package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单状态
 */
@Data
public class OrderStatusVO {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 缴费项目ID
     */
    private String itemId;

    /**
     * 缴费项目名称
     */
    private String itemName;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付方式：ALIPAY-支付宝，WECHAT-微信支付
     */
    private String paymentMethod;

    /**
     * 订单状态：
     * PENDING-待支付
     * PAID-已支付
     * CANCELLED-已取消
     * EXPIRED-已过期
     * REFUNDING-退款中
     * REFUNDED-已退款
     */
    private String status;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 支付流水号
     */
    private String tradeNo;

    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;

    /**
     * 订单过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 退款状态：
     * null-未退款
     * PROCESSING-退款处理中
     * SUCCESS-退款成功
     * FAILED-退款失败
     */
    private String refundStatus;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 退款流水号
     */
    private String refundNo;
}