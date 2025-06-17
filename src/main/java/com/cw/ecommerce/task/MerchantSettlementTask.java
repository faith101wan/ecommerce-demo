package com.cw.ecommerce.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cw.ecommerce.common.enumerate.Status;
import com.cw.ecommerce.model.Merchant;
import com.cw.ecommerce.repositories.MerchantRepository;
import com.cw.ecommerce.repositories.ProductRepository;
import com.cw.ecommerce.repositories.TransactionLogRepository;
import com.cw.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantSettlementTask {

    private final MerchantRepository merchantRepository;
    private final OrderService orderService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void runDailySettlement() {
        LocalDateTime startTime = LocalDate.now().atStartOfDay();
        LocalDateTime endTime = LocalDateTime.now();

        // 1. 获取所有活跃商家
        QueryWrapper<Merchant> merchantQueryWrapper = new QueryWrapper<>();
        merchantQueryWrapper.eq("status", Status.ACTIVE.getCode());
        List<Merchant> merchantList = merchantRepository.selectList(merchantQueryWrapper);

        // 2. 并行处理每个商家
        merchantList.forEach(this::settleSingleMerchant);
    }

    private void settleSingleMerchant(Merchant merchant) {
        try {
            orderService.settleSingleMerchant(merchant);
        } catch (Exception e) {
            log.error("Merchant settlement failed: {}", merchant.getMerchantId(), e);
            // todo 可记录失败状态，用于后续补偿
        }
    }
}
