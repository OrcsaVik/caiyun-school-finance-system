package com.github.caiyun.pay.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付订单实体
 */
@Data
@TableName("payment_order")
public class PaymentOrderDO {

    /**
     * 订单ID
     */
    @TableId
    private String id;

    /**
     * 缴费项目ID
     */
    private String itemId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付方式：ALIPAY-支付宝，WECHAT-微信支付
     */
    private String paymentMethod;

    /**
     * 支付场景：APP-APP支付，H5-H5支付，JSAPI-公众号支付，NATIVE-扫码支付
     */
    private String paymentScene;

    /**
     * 支付终端：IOS-苹果，ANDROID-安卓，WEB-网页，WECHAT-微信
     */
    private String paymentTerminal;

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
     * 支付参数，JSON格式，根据支付方式和场景返回不同的参数
     */
    private String payParams;

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

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Boolean deleted;
}