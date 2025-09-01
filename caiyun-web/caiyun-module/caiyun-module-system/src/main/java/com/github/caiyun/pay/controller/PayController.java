package com.github.caiyun.pay.controller;

import com.github.caiyun.common.domain.CommonResult;
import com.github.caiyun.pay.model.vo.*;
import com.github.caiyun.pay.service.PayService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Resource
    private PayService payService;

    @GetMapping("/items")
    public CommonResult<PaymentItemPageVO> getPaymentItems(PaymentItemQueryVO query) {
        return CommonResult.success(payService.getPaymentItems(query));
    }

    @GetMapping("/items/{itemId}")
    public CommonResult<PaymentItemDetailVO> getPaymentItemDetail(@PathVariable String itemId) {
        return CommonResult.success(payService.getPaymentItemDetail(itemId));
    }

    @PostMapping("/orders")
    public CommonResult<CreateOrderResponseVO> createOrder(@RequestParam String userId, @RequestBody @Valid CreateOrderRequestVO request) {
        return CommonResult.success(payService.createOrder(userId, request));
    }

    @GetMapping("/orders/{orderId}/status")
    public CommonResult<OrderStatusVO> getOrderStatus(@PathVariable String orderId) {
        return CommonResult.success(payService.getOrderStatus(orderId));
    }

    @GetMapping("/statistics")
    public CommonResult<PaymentStatisticsVO> getPaymentStatistics(@RequestParam String userId) {
        return CommonResult.success(payService.getPaymentStatistics(userId));
    }

    /**
     * 获取支付历史
     * @param userId
     * @param query
     * @return
     */
    @GetMapping("/history")
    public CommonResult<PaymentHistoryPageVO> getPaymentHistory(@RequestParam String userId, PaymentHistoryQueryVO query) {
        return CommonResult.success(payService.getPaymentHistory(userId, query));
    }

    /**
     * 申请退款
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/refunds")
    public CommonResult<RefundResponseVO> applyRefund(@RequestParam String userId, @RequestBody @Valid RefundRequestVO request) {
        return CommonResult.success(payService.applyRefund(userId, request));
    }

    @GetMapping("/refunds/{refundId}/status")
    public CommonResult<RefundStatusVO> getRefundStatus(@PathVariable String refundId) {
        return CommonResult.success(payService.getRefundStatus(refundId));
    }
}