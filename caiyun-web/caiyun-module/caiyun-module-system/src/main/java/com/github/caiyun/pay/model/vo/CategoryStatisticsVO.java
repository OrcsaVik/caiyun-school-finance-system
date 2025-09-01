package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Vik
 * @date 2025-08-29 22:59
 * @description
 */
@Data
public class CategoryStatisticsVO {
    private String category;
    private Long count;
    private BigDecimal amount;
    private Long userCount;
}
