package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 支付历史分页响应
 */
@Data
public class PaymentHistoryPageVO {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 支付历史列表
     */
    private List<PaymentHistoryVO> list;

    /**
     * 支付历史记录
     */
    @Data
    public static class PaymentHistoryVO {
        /**
         * 订单ID
         */
        private String orderId;

        /**
         * 缴费项目ID
         */
        private String itemId;

        /**
         * 缴费项目名称
         */
        private String itemName;

        /**
         * 项目类别
         */
        private String category;

        /**
         * 支付金额
         */
        private BigDecimal amount;

        /**
         * 支付方式：ALIPAY-支付宝，WECHAT-微信支付
         */
        private String paymentMethod;

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
         * 订单创建时间
         */
        private LocalDateTime createTime;

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
    }

    /**
     * 创建分页响应
     */
    public static PaymentHistoryPageVO of(List<PaymentHistoryVO> list, Long total, Integer pageNum, Integer pageSize) {
        PaymentHistoryPageVO page = new PaymentHistoryPageVO();
        page.setList(list);
        page.setTotal(total);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPages((int) Math.ceil((double) total / pageSize));
        return page;
    }
}