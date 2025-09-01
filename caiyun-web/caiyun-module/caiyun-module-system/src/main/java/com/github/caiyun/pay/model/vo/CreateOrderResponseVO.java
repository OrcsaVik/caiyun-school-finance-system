package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 创建支付订单响应
 */
@Data
public class CreateOrderResponseVO {

    /**
     * 订单ID
     */
    private String orderId;

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
     * 支付参数，根据支付方式和场景返回不同的参数：
     * 1. 支付宝
     *   - APP：app_params，客户端通过SDK调用
     *   - H5：h5_url，跳转到支付宝H5支付页面
     *   - NATIVE：qr_code，二维码图片的Base64编码
     * 2. 微信支付
     *   - APP：app_params，客户端通过SDK调用
     *   - H5：h5_url，跳转到微信H5支付页面
     *   - JSAPI：js_params，公众号JSSDK调用参数
     *   - NATIVE：qr_code，二维码图片的Base64编码
     */
    private String payParams;

    /**
     * 订单有效期（秒）
     */
    private Integer expireSeconds;

    private String status;

    private LocalDateTime payTime;

    private String tradeNo;
}