package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单VO，用于前端展示
 */
@Data
public class PaymentOrderVO {

    /**
     * 订单ID
     */
    private String id;

    /**
     * 缴费项目信息
     */
    private PaymentItemVO paymentItem;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付方式：ALIPAY-支付宝，WECHAT-微信支付
     */
    private String paymentMethod;

    /**
     * 订单状态：待支付、已支付、已取消、已过期、退款中、已退款
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
     * 订单过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 退款状态：未退款、退款处理中、退款成功、退款失败
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

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}