package com.github.caiyun.pay.service;

import com.github.caiyun.pay.model.vo.*;

/**
 * 支付服务接口
 */
public interface PayService {

    /**
     * 获取缴费项目列表
     *
     * @param query 查询条件
     * @return 缴费项目分页数据
     */
    PaymentItemPageVO getPaymentItems(PaymentItemQueryVO query);

    /**
     * 获取缴费项目详情
     *
     * @param itemId 项目ID
     * @return 缴费项目详情
     */
    PaymentItemDetailVO getPaymentItemDetail(String itemId);

    /**
     * 创建支付订单
     *
     * @param request 创建订单请求
     * @return 创建订单响应
     */
    CreateOrderResponseVO createOrder(String userId, CreateOrderRequestVO request);

    /**
     * 查询支付订单状态
     *
     * @param orderId 订单ID
     * @return 订单状态
     */
    OrderStatusVO getOrderStatus(String orderId);

    /**
     * 获取支付统计信息
     *
     * @return 支付统计信息
     */
    PaymentStatisticsVO getPaymentStatistics(String userId);

    /**
     * 获取支付历史记录
     *
     * @param query 查询条件
     * @return 支付历史分页数据
     */
    PaymentHistoryPageVO getPaymentHistory(String userId,PaymentHistoryQueryVO query);

    /**
     * 申请退款
     *
     * @param request 退款申请请求
     * @return 退款申请响应
     */
    RefundResponseVO applyRefund(String userId,RefundRequestVO request);

    /**
     * 查询退款状态
     *
     * @param refundId 退款ID
     * @return 退款状态
     */
    RefundStatusVO getRefundStatus(String refundId);
}