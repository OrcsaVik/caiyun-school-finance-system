package com.github.caiyun.pay.util;

import com.github.caiyun.pay.constant.PayConstants;
import com.github.caiyun.pay.exception.PayException;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 支付工具类
 */
public class PayUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 生成订单ID
     * 格式：yyyyMMddHHmmss + 8位随机数
     *
     * @return 订单ID
     */
    public static String generateOrderId() {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String random = String.format("%08d", (int) (Math.random() * 100000000));
        return timestamp + random;
    }

    /**
     * 生成退款ID
     * 格式：R + yyyyMMddHHmmss + 8位随机数
     *
     * @return 退款ID
     */
    public static String generateRefundId() {
        String timestamp = LocalDateTime.now().format(DATE_TIME_FORMATTER);
        String random = String.format("%08d", (int) (Math.random() * 100000000));
        return "R" + timestamp + random;
    }

    /**
     * 生成支付流水号
     *
     * @return 支付流水号
     */
    public static String generateTradeNo() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 计算订单过期时间
     *
     * @return 过期时间
     */
    public static LocalDateTime calculateOrderExpireTime() {
        return LocalDateTime.now().plusMinutes(PayConstants.ORDER_EXPIRE_MINUTES);
    }

    /**
     * 校验退款金额
     *
     * @param orderAmount  订单金额（分）
     * @param refundAmount 退款金额（分）
     * @throws PayException 退款金额超过订单金额时抛出异常
     */
    public static void validateRefundAmount(Long orderAmount, @NotNull(message = "退款金额不能为空") Long refundAmount) {
        if (refundAmount <= 0) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "退款金额必须大于0");
        }
        if (refundAmount > orderAmount) {
            throw new PayException(PayException.ErrorCode.EXCEED_MAX_REFUND_AMOUNT, "退款金额不能超过订单金额");
        }
    }

    /**
     * 校验支付方式是否有效
     *
     * @param paymentMethod 支付方式
     * @throws PayException 支付方式无效时抛出异常
     */
    public static void validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "支付方式不能为空");
        }
        boolean valid = PayConstants.PaymentMethod.ALIPAY.equals(paymentMethod) ||
                PayConstants.PaymentMethod.WECHAT.equals(paymentMethod) ||
                PayConstants.PaymentMethod.UNIONPAY.equals(paymentMethod);
        if (!valid) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "无效的支付方式");
        }
    }

    /**
     * 校验支付场景是否有效
     *
     * @param paymentScene 支付场景
     * @throws PayException 支付场景无效时抛出异常
     */
    public static void validatePaymentScene(String paymentScene) {
        if (paymentScene == null || paymentScene.trim().isEmpty()) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "支付场景不能为空");
        }
        boolean valid = PayConstants.PaymentScene.PC.equals(paymentScene) ||
                PayConstants.PaymentScene.H5.equals(paymentScene) ||
                PayConstants.PaymentScene.APP.equals(paymentScene) ||
                PayConstants.PaymentScene.MINI_PROGRAM.equals(paymentScene);
        if (!valid) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "无效的支付场景");
        }
    }

    /**
     * 校验支付终端是否有效
     *
     * @param paymentTerminal 支付终端
     * @throws PayException 支付终端无效时抛出异常
     */
    public static void validatePaymentTerminal(String paymentTerminal) {
        if (paymentTerminal == null || paymentTerminal.trim().isEmpty()) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "支付终端不能为空");
        }
        boolean valid = PayConstants.PaymentTerminal.PC_BROWSER.equals(paymentTerminal) ||
                PayConstants.PaymentTerminal.MOBILE_BROWSER.equals(paymentTerminal) ||
                PayConstants.PaymentTerminal.ANDROID_APP.equals(paymentTerminal) ||
                PayConstants.PaymentTerminal.IOS_APP.equals(paymentTerminal) ||
                PayConstants.PaymentTerminal.WECHAT_MINI_PROGRAM.equals(paymentTerminal);
        if (!valid) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "无效的支付终端");
        }
    }

    /**
     * 校验缴费项目状态是否有效
     *
     * @param status 缴费项目状态
     * @throws PayException 缴费项目状态无效时抛出异常
     */
    public static void validatePaymentItemStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "缴费项目状态不能为空");
        }
        boolean valid = PayConstants.PaymentItemStatus.NOT_STARTED.equals(status) ||
                PayConstants.PaymentItemStatus.IN_PROGRESS.equals(status) ||
                PayConstants.PaymentItemStatus.ENDED.equals(status);
        if (!valid) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "无效的缴费项目状态");
        }
    }

    /**
     * 校验缴费项目类别是否有效
     *
     * @param category 缴费项目类别
     * @throws PayException 缴费项目类别无效时抛出异常
     */
    public static void validatePaymentItemCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "缴费项目类别不能为空");
        }
        boolean valid = PayConstants.PaymentItemCategory.TUITION.equals(category) ||
                PayConstants.PaymentItemCategory.ACCOMMODATION.equals(category) ||
                PayConstants.PaymentItemCategory.TEXTBOOK.equals(category) ||
                PayConstants.PaymentItemCategory.INSURANCE.equals(category) ||
                PayConstants.PaymentItemCategory.OTHER.equals(category);
        if (!valid) {
            throw new PayException(PayException.ErrorCode.INVALID_PARAMETER, "无效的缴费项目类别");
        }
    }
}