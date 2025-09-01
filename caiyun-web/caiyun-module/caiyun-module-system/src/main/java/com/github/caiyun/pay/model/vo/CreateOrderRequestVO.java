package com.github.caiyun.pay.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建支付订单请求
 */
@Data
public class CreateOrderRequestVO {

    /**
     * 缴费项目ID
     */
    @NotBlank(message = "缴费项目ID不能为空")
    private String itemId;

    /**
     * 支付方式：ALIPAY-支付宝，WECHAT-微信支付
     */
    @NotBlank(message = "支付方式不能为空")
    private String paymentMethod;

    /**
     * 支付场景：APP-APP支付，H5-H5支付，JSAPI-公众号支付，NATIVE-扫码支付
     */
    @NotBlank(message = "支付场景不能为空")
    private String paymentScene;

    /**
     * 支付终端：IOS-苹果，ANDROID-安卓，WEB-网页，WECHAT-微信
     */
    @NotBlank(message = "支付终端不能为空")
    private String paymentTerminal;

    /**
     * 备注信息
     */
    private String remark;
}