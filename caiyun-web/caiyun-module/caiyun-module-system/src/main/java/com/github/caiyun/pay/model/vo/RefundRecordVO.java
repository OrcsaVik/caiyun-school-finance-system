package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录VO，用于前端展示
 */
@Data
public class RefundRecordVO {

    /**
     * 退款ID
     */
    private String id;

    /**
     * 支付订单信息
     */
    private PaymentOrderVO paymentOrder;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 退款状态：退款处理中、退款成功、退款失败
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
     * 失败原因
     */
    private String failReason;

    /**
     * 预计退款到账时间
     */
    private LocalDateTime expectedArrivalTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}