package com.github.caiyun.pay.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.caiyun.pay.constant.PayConstants;
import com.github.caiyun.pay.exception.PayException;
import com.github.caiyun.mapper.PaymentItemMapper;
import com.github.caiyun.mapper.PaymentOrderMapper;
import com.github.caiyun.mapper.RefundRecordMapper;
import com.github.caiyun.pay.model.entity.PaymentItemDO;
import com.github.caiyun.pay.model.entity.PaymentOrderDO;
import com.github.caiyun.pay.model.entity.RefundRecordDO;
import com.github.caiyun.pay.model.vo.*;
import com.github.caiyun.pay.service.PayService;
import com.github.caiyun.pay.util.PayUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付服务实现类
 */
@Service
public class PayServiceImpl implements PayService {

    @Resource
    private PaymentItemMapper paymentItemMapper;

    @Resource
    private PaymentOrderMapper paymentOrderMapper;

    @Resource
    private RefundRecordMapper refundRecordMapper;

    /**
     * 获取支付列表
     * @param query 查询条件
     * @return
     */
    @Override
    public PaymentItemPageVO getPaymentItems(PaymentItemQueryVO query) {
        // 构建查询条件
        Page<PaymentItemDO> page = new Page<>(query.getPageNum(), query.getPageSize());
        page = (Page<PaymentItemDO>) paymentItemMapper.selectPage(page, query.getName(), query.getCategory(), query.getStatus());

        // 转换为VO对象
        List<PaymentItemVO> items = page.getRecords().stream().map(item -> {
            PaymentItemVO vo = new PaymentItemVO();
            BeanUtils.copyProperties(item, vo);
            return vo;
        }).collect(Collectors.toList());

        return PaymentItemPageVO.of(items,
                page.getTotal(), query.getPageNum(), query.getPageSize());
    }

    @Override
    public PaymentItemDetailVO getPaymentItemDetail(String itemId) {
        PaymentItemDO item = paymentItemMapper.selectById(itemId);
        if (item == null || item.getDeleted()) {
            throw new PayException(PayException.ErrorCode.PAYMENT_ITEM_NOT_FOUND, "缴费项目不存在");
        }

        PaymentItemDetailVO detail = new PaymentItemDetailVO();
        //赋值对应返回类
        BeanUtils.copyProperties(item, detail);
        return detail;
    }

    /**
     * 手动进行获取对用userid
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public CreateOrderResponseVO createOrder(String userId, CreateOrderRequestVO request) {
        // 校验支付参数 String userId,
        PayUtils.validatePaymentMethod(request.getPaymentMethod());
        PayUtils.validatePaymentScene(request.getPaymentScene());
        PayUtils.validatePaymentTerminal(request.getPaymentTerminal());

        // 查询缴费项目
        PaymentItemDO item = paymentItemMapper.selectById(request.getItemId());
        if (item == null || item.getDeleted()) {
            throw new PayException(PayException.ErrorCode.PAYMENT_ITEM_NOT_FOUND, "缴费项目不存在");
        }

        // 校验缴费项目状态
        if (!PayConstants.PaymentItemStatus.IN_PROGRESS.equals(item.getStatus())) {
            throw new PayException(PayException.ErrorCode.INVALID_PAYMENT_ITEM_STATUS, "缴费项目不在缴费期间");
        }



        // 创建支付订单
        PaymentOrderDO order = new PaymentOrderDO();
        order.setId(PayUtils.generateOrderId());
        order.setItemId(item.getId());
        order.setUserId(userId);
        order.setAmount(item.getActualAmount());
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentScene(request.getPaymentScene());
        order.setPaymentTerminal(request.getPaymentTerminal());
        order.setStatus(PayConstants.OrderStatus.PENDING);
        order.setPayTime(LocalDateTime.now());
        order.setTradeNo(PayUtils.generateOrderId());
        //设置过期10分钟
        order.setExpireTime(LocalDateTime.now().plusMinutes(10));
        order.setRemark(request.getRemark());
        paymentOrderMapper.insert(order);

        // 构建响应对象 创建订单 默认死数据
        CreateOrderResponseVO response = new CreateOrderResponseVO();
        response.setOrderId(order.getId());
        response.setAmount(order.getAmount());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentScene(order.getPaymentScene());
        response.setPayParams("{}");
        response.setStatus(PayConstants.OrderStatus.PENDING);
        response.setPayTime(order.getPayTime());
        response.setTradeNo(order.getTradeNo());
        return response;
    }

    @Override
    public OrderStatusVO getOrderStatus(String orderId) {
        PaymentOrderDO order = paymentOrderMapper.selectById(orderId);
        if (order == null || order.getDeleted()) {
            throw new PayException(PayException.ErrorCode.ORDER_NOT_FOUND, "订单不存在");
        }

        PaymentItemDO item = paymentItemMapper.selectById(order.getItemId());
        OrderStatusVO status = new OrderStatusVO();
        status.setOrderId(order.getId());
        status.setItemId(order.getItemId());
        status.setItemName(item.getName());
        status.setAmount(order.getAmount());
        status.setPaymentMethod(order.getPaymentMethod());
        status.setStatus(PayConstants.OrderStatus.PAID);
        status.setPayTime( LocalDateTime.now());
        status.setTradeNo(PayUtils.generateOrderId());
        status.setCreateTime(order.getCreateTime());
        status.setExpireTime(order.getExpireTime());
        
        // 更新订单状态为支付成功
        order.setStatus(PayConstants.OrderStatus.PAID);
        order.setPayTime(status.getPayTime());
        order.setTradeNo(status.getTradeNo());
        paymentOrderMapper.updateById(order);
        
        return status;
    }

    @Override
    public PaymentStatisticsVO getPaymentStatistics(String userId) {
        Map<String, Object> pendingStats = paymentOrderMapper.selectPendingStatistics(userId);
        Map<String, Object> paidStats = paymentOrderMapper.selectPaidStatistics(userId);
        Map<String, Object> refundStats = paymentOrderMapper.selectRefundStatistics(userId);
        List<Map<String, Object>> categoryStats = paymentOrderMapper.selectCategoryStatistics(userId);
        List<Map<String, Object>> monthlyTrend = paymentOrderMapper.selectMonthlyTrend(userId);

        PaymentStatisticsVO statistics = new PaymentStatisticsVO();
        statistics.setPendingItemCount((Long) pendingStats.get("count"));
        statistics.setPendingAmount((BigDecimal) pendingStats.get("amount"));
        statistics.setPaidItemCount((Long) paidStats.get("count"));
        statistics.setPaidAmount((BigDecimal) paidStats.get("amount"));
        statistics.setRefundItemCount((Long) refundStats.get("count"));
        statistics.setRefundAmount((BigDecimal) refundStats.get("amount"));

        //分类状态数据进行加工处理
        List<CategoryStatisticsVO> categories = categoryStats.stream().map(stat -> {
            CategoryStatisticsVO category = new CategoryStatisticsVO();
            category.setCategory((String) stat.get("category"));
            category.setCount((Long) stat.get("count"));
            category.setAmount((BigDecimal) stat.get("amount"));
            category.setUserCount((Long) stat.get("userCount"));
            return category;
        }).collect(Collectors.toList());
        statistics.setCategoryStatistics(categories);

        List<MonthlyTrendVO> trends = monthlyTrend.stream().map(trend -> {
            MonthlyTrendVO monthTrend = new MonthlyTrendVO();
            monthTrend.setMonth((String) trend.get("month"));
            monthTrend.setCount((Long) trend.get("count"));
            monthTrend.setAmount((BigDecimal) trend.get("amount"));
            monthTrend.setUserCount((Long) trend.get("userCount"));
            return monthTrend;
        }).collect(Collectors.toList());
        statistics.setMonthlyTrend(trends);

        return statistics;
    }

    @Override
    public PaymentHistoryPageVO getPaymentHistory(String userId, PaymentHistoryQueryVO query) {
        // 构建查询条件

        Page<PaymentOrderDO> page = new Page<>(query.getPageNum(), query.getPageSize());
        page = (Page<PaymentOrderDO>) paymentOrderMapper.selectHistoryPage(page, userId, query);

        // 转换为VO对象
        List<PaymentHistoryPageVO.PaymentHistoryVO> history = page.getRecords().stream().map(order -> {
            PaymentHistoryPageVO.PaymentHistoryVO vo = new PaymentHistoryPageVO.PaymentHistoryVO();
            BeanUtils.copyProperties(order, vo);
            return vo;
        }).collect(Collectors.toList());

        return PaymentHistoryPageVO.of(history, page.getTotal(), query.getPageNum(), query.getPageSize());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RefundResponseVO applyRefund(String userId, RefundRequestVO request) {
        // 查询订单信息
        PaymentOrderDO order = paymentOrderMapper.selectById(request.getOrderId());
        if (order == null || order.getDeleted()) {
            throw new PayException(PayException.ErrorCode.ORDER_NOT_FOUND, "订单不存在");
        }

        // TODO校验订单状态 不进行检验 退款 将对应关闭的订单进行退款
//        if (!PayConstants.OrderStatus.CLOSED.equals(order.getStatus())) {
//            throw new PayException(PayException.ErrorCode.INVALID_ORDER_STATUS, "订单状态不正确");
//        }

        // 校验退款金额
        PayUtils.validateRefundAmount(order.getAmount().longValue(), request.getAmount().longValue());

        // 查询已退款金额 需要有对应成功数据
        Long refundedAmount = refundRecordMapper.selectTotalAmountByOrderId(order.getId());
        // 假设 refundedAmount 是 double 或 int，request.getAmount() 返回 BigDecimal
        BigDecimal totalRefund = BigDecimal.valueOf(refundedAmount).add(request.getAmount());
        int comparisonResult = totalRefund.compareTo(order.getAmount());

        if (comparisonResult > 0) {
            // totalRefund > order.getAmount()
            // 抛出异常或处理超额退款
            throw new IllegalArgumentException("退款总额超过订单金额");
        }

        // 创建退款记录
        RefundRecordDO record = new RefundRecordDO();
        record.setId(PayUtils.generateRefundId());
        record.setOrderId(order.getId());
        record.setUserId(userId);
        record.setAmount(request.getAmount());
        record.setStatus(PayConstants.RefundStatus.SUCCESS);
        record.setReason(request.getReason());
        record.setDescription(request.getDescription());
        record.setRefundTime(LocalDateTime.now());
        record.setRefundNo(PayUtils.generateRefundId());
        refundRecordMapper.insert(record);

        // 更新订单退款状态
        order.setRefundStatus(PayConstants.RefundStatus.SUCCESS);
        order.setRefundAmount(BigDecimal.valueOf(refundedAmount).add(request.getAmount()));
        order.setRefundTime(record.getRefundTime());
        order.setRefundNo(record.getRefundNo());
        paymentOrderMapper.updateById(order);

        // 构建响应对象
        RefundResponseVO response = new RefundResponseVO();
        BeanUtils.copyProperties(record, response);
        return response;
    }

    @Override
    public RefundStatusVO getRefundStatus(String refundId) {
        RefundRecordDO record = refundRecordMapper.selectById(refundId);
        if (record == null || record.getDeleted()) {
            throw new PayException(PayException.ErrorCode.REFUND_FAILED, "退款记录不存在");
        }

        PaymentOrderDO order = paymentOrderMapper.selectById(record.getOrderId());
        PaymentItemDO item = paymentItemMapper.selectById(order.getItemId());

        RefundStatusVO status = new RefundStatusVO();
        status.setRefundId(record.getId());
        status.setOrderId(record.getOrderId());
        status.setItemId(order.getItemId());
        status.setItemName(item.getName());
        status.setAmount(record.getAmount());
        status.setStatus(record.getStatus());
        status.setReason(record.getReason());
        status.setDescription(record.getDescription());
        status.setRefundNo(record.getRefundNo());
        status.setRefundTime(record.getRefundTime());
        status.setCreateTime(record.getCreateTime());
        status.setFailReason(record.getFailReason());
        status.setExpectedArrivalTime(record.getExpectedArrivalTime());
        return status;
    }
}