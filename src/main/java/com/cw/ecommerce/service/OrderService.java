package com.cw.ecommerce.service;

import com.cw.ecommerce.config.BusinessException;
import com.cw.ecommerce.model.*;
import com.cw.ecommerce.model.request.OrderRequest;
import com.cw.ecommerce.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final MerchantSettlementRepository merchantSettlementRepository;


    @Transactional(rollbackFor = Exception.class)
    public void placeOrder(OrderRequest request) {
        // 1. 校验用户和商品
        User user = userRepository.selectById(request.getUserId());
        Product product = productRepository.selectBySku(request.getSku());

        if (product == null || user == null) {
            throw new BusinessException("parameter is not correct");
        }

        // 2. 检查库存和余额
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        if (product.getStock() < request.getQuantity()) {
            throw new BusinessException("库存不足");
        }
        if (user.getBalance().compareTo(totalPrice) < 0) {
            throw new BusinessException("余额不足");
        }

        // 3. 扣减库存和余额
        int productCount = productRepository.deductStock(product.getId(), request.getQuantity());
        if (productCount < request.getQuantity()) {
            throw new BusinessException("库存不足");
        }
        int userCount = userRepository.updateBalance(user.getUserId(), totalPrice.negate());
        if (userCount == 0) {
            throw new BusinessException("余额不足");
        }
        int merchantCount = merchantRepository.updateBalance(product.getMerchantId(), totalPrice);
        if (merchantCount == 0) {
            throw new BusinessException("商家余额不足");
        }
        // 4. 记录交易流水
        TransactionLog transaction = new TransactionLog();
        transaction.setUserId(user.getUserId());
        transaction.setProductId(product.getId());
        transaction.setAmount(totalPrice);
        transaction.setQuantity(request.getQuantity());
        transaction.setMerchantId(product.getMerchantId());
        transactionLogRepository.insert(transaction);
    }


    @Transactional(rollbackFor = Exception.class)
    public void settleSingleMerchant(Merchant merchant) {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDateTime.now();

        // 3. 计算理论销售额
        BigDecimal expectedIncome = transactionLogRepository.sumSalesByMerchant(merchant.getMerchantId(), start, end);

        // 4. 获取实际账户余额
        BigDecimal actualIncome = merchant.getBalance();

        // 5. 获取商品销量
        List<Map<String, Object>> salesData = transactionLogRepository.sumProductSales(
                merchant.getMerchantId(), start, end);
        Map<Long, Integer> productSales = salesData.stream()
                .collect(Collectors.toMap(
                        map -> Long.parseLong(map.get("product_id").toString()),
                        map -> Integer.parseInt(map.get("total").toString())
                ));

        // 6. 保存结算记录
        MerchantSettlement settlement = new MerchantSettlement();
        settlement.setMerchantId(merchant.getMerchantId());
        settlement.setExpectedAmount(expectedIncome);
        settlement.setActualAmount(actualIncome);
        settlement.setStatus(expectedIncome.compareTo(actualIncome) == 0 ? "SUCCESS" : "FAILED");
        settlement.setProductCount(productSales.size());
        merchantSettlementRepository.insert(settlement);

        // 7. todo 不一致时触发告警

    }
}