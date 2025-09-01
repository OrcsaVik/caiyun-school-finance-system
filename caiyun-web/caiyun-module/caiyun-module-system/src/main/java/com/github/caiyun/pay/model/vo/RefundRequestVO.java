package com.github.caiyun.pay.model.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 退款申请请求
 */
@Data
public class RefundRequestVO {

    /**
     * 订单ID
     */
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    /**
     * 退款金额
     */
    @NotNull(message = "退款金额不能为空")
    private BigDecimal amount;

    /**
     * 退款原因
     */
    @NotBlank(message = "退款原因不能为空")
    @Size(max = 200, message = "退款原因不能超过200个字符")
    private String reason;

    /**
     * 退款说明
     */
    @Size(max = 500, message = "退款说明不能超过500个字符")
    private String description;
}