package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 支付统计信息
 */
@Data
public class PaymentStatisticsVO {

    /**
     * 待缴费项目数量
     */
    private Long pendingItemCount;

    /**
     * 待缴费总金额
     */
    private BigDecimal pendingAmount;

    /**
     * 已缴费项目数量
     */
    private Long paidItemCount;

    /**
     * 已缴费总金额
     */
    private BigDecimal paidAmount;

    /**
     * 退款项目数量
     */
    private Long refundItemCount;

    /**
     * 退款总金额
     */
    private BigDecimal refundAmount;

    /**
     * 各缴费项目类别的统计
     */
    private List<CategoryStatisticsVO> categoryStatistics;

    /**
     * 近6个月的缴费趋势
     */
    private List<MonthlyTrendVO> monthlyTrend;


}