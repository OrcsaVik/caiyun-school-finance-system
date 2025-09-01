package com.github.caiyun.pay.exception;

import lombok.Data;

/**
 * 支付模块异常类
 */
@Data
public class PayException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误消息
     */
    private String message;

    /**
     * 错误数据
     */
    private Object data;

    public PayException(String message) {
        super(message);
        this.message = message;
    }

    public PayException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public PayException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public PayException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }


    /**
     * 支付异常错误码
     */
    public static class ErrorCode {
        /** 参数错误 */
        public static final String INVALID_PARAMETER = "PAY_INVALID_PARAMETER";
        /** 订单不存在 */
        public static final String ORDER_NOT_FOUND = "PAY_ORDER_NOT_FOUND";
        /** 订单已过期 */
        public static final String ORDER_EXPIRED = "PAY_ORDER_EXPIRED";
        /** 订单状态错误 */
        public static final String INVALID_ORDER_STATUS = "PAY_INVALID_ORDER_STATUS";
        /** 支付失败 */
        public static final String PAYMENT_FAILED = "PAY_PAYMENT_FAILED";
        /** 退款失败 */
        public static final String REFUND_FAILED = "PAY_REFUND_FAILED";
        /** 超过最大退款次数 */
        public static final String EXCEED_MAX_REFUND_TIMES = "PAY_EXCEED_MAX_REFUND_TIMES";
        /** 超过最大退款金额 */
        public static final String EXCEED_MAX_REFUND_AMOUNT = "PAY_EXCEED_MAX_REFUND_AMOUNT";
        /** 缴费项目不存在 */
        public static final String PAYMENT_ITEM_NOT_FOUND = "PAY_PAYMENT_ITEM_NOT_FOUND";
        /** 缴费项目已过期 */
        public static final String PAYMENT_ITEM_EXPIRED = "PAY_PAYMENT_ITEM_EXPIRED";
        /** 缴费项目状态错误 */
        public static final String INVALID_PAYMENT_ITEM_STATUS = "PAY_INVALID_PAYMENT_ITEM_STATUS";
        /** 系统错误 */
        public static final String SYSTEM_ERROR = "PAY_SYSTEM_ERROR";
    }
}