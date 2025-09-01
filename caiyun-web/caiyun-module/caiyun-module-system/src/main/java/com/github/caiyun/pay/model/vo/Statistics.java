package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Vik
 * @date 2025-08-29 22:59
 * @description
 */
@Data
public class Statistics {
    private List<CategoryStatisticsVO> categories;
    private List<MonthlyTrendVO> trends;
}