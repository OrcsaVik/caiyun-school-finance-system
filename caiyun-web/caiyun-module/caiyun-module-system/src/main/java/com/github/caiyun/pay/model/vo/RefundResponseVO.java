package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款申请响应
 */
@Data
public class RefundResponseVO {

    /**
     * 退款ID
     */
    private String refundId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 退款状态：
     * PROCESSING-退款处理中
     * SUCCESS-退款成功
     * FAILED-退款失败
     */
    private String status;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款说明
     */
    private String description;

    /**
     * 退款流水号
     */
    private String refundNo;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}