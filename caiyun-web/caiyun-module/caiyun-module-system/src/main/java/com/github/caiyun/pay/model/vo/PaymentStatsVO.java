package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付统计VO，用于前端展示统计信息
 */
@Data
public class PaymentStatsVO {

    /**
     * 待缴费项目数量
     */
    private Integer pendingCount;

    /**
     * 已完成项目数量
     */
    private Integer completedCount;

    /**
     * 待缴总金额
     */
    private BigDecimal totalPendingAmount;

    /**
     * 近期截止项目数量（30天内）
     */
    private Integer upcomingDeadlines;

    /**
     * 已逾期项目数量
     */
    private Integer overdueCount;

    /**
     * 本月已完成项目数量
     */
    private Integer monthlyCompleted;

    /**
     * 已完成项目总金额
     */
    private BigDecimal totalCompletedAmount;

    /**
     * 最高待缴金额
     */
    private BigDecimal highestPendingAmount;

    /**
     * 平均待缴金额
     */
    private BigDecimal averagePendingAmount;
}