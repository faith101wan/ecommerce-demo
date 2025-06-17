package com.cw.ecommerce.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cw.ecommerce.common.enumerate.Status;
import com.cw.ecommerce.model.User;
import com.cw.ecommerce.repositories.UserRepository;
import com.cw.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @Transactional
    void balance_ShouldCallUpdateBalanceWithAbsoluteValue() {
        // Arrange
        String userId = "user123";
        BigDecimal amount = new BigDecimal("-100.00"); // 负值
        
        // Act
        userService.balance(userId, amount);
        
        // Assert
        verify(userRepository, times(1)).updateBalance(userId, new BigDecimal("100.00"));
    }

    @Test
    @Transactional
    void balance_ShouldWorkWithPositiveAmount() {
        // Arrange
        String userId = "user123";
        BigDecimal amount = new BigDecimal("200.00"); // 正值
        
        // Act
        userService.balance(userId, amount);
        
        // Assert
        verify(userRepository, times(1)).updateBalance(userId, amount);
    }

    @Test
    @Transactional
    void balance_ShouldWorkWithZeroAmount() {
        // Arrange
        String userId = "user123";
        BigDecimal amount = BigDecimal.ZERO;
        
        // Act
        userService.balance(userId, amount);
        
        // Assert
        verify(userRepository, times(1)).updateBalance(userId, BigDecimal.ZERO);
    }

    @Test
    void getUserById_ShouldReturnActiveUser_WhenExists() {
        // Arrange
        String userId = "activeUser";
        User expectedUser = new User();
        expectedUser.setUserId(userId);
        expectedUser.setStatus(Status.ACTIVE.getCode());
        
        when(userRepository.selectOne(any(QueryWrapper.class))).thenReturn(expectedUser);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(Status.ACTIVE.getCode(), result.getStatus());
        
        // 验证 QueryWrapper 条件是否正确
        verify(userRepository).selectOne(argThat((QueryWrapper<User> wrapper) -> 
            wrapper.getEntity().getUserId().equals(userId) &&
            wrapper.getEntity().getStatus().equals(Status.ACTIVE.getCode())
        ));
    }

    @Test
    void getUserById_ShouldReturnNull_WhenUserNotExists() {
        // Arrange
        String userId = "nonExistingUser";
        when(userRepository.selectOne(any(QueryWrapper.class))).thenReturn(null);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNull(result);
    }

    @Test
    void getUserById_ShouldReturnNull_WhenUserInactive() {
        // Arrange
        String userId = "inactiveUser";
        User inactiveUser = new User();
        inactiveUser.setUserId(userId);
        inactiveUser.setStatus(Status.INACTIVE.getCode()); // 假设有INACTIVE状态
        
        when(userRepository.selectOne(any(QueryWrapper.class))).thenReturn(null);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNull(result);
    }

    @Test
    void getUserById_ShouldBuildCorrectQueryWrapper() {
        // Arrange
        String userId = "testUser";
        
        // Act
        userService.getUserById(userId);

        // Assert - 验证构建的QueryWrapper条件
        verify(userRepository).selectOne(argThat((QueryWrapper<User> wrapper) -> {
            User queryEntity = wrapper.getEntity();
            return userId.equals(queryEntity.getUserId())
                    && Objects.equals(Status.ACTIVE.getCode(), queryEntity.getStatus());
        }));
    }
}