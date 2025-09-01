package com.github.caiyun.pay.model.vo;

import lombok.Data;

/**
 * 支付历史查询条件
 */
@Data
public class PaymentHistoryQueryVO {

    /**
     * 缴费项目名称，模糊匹配
     */
    private String itemName;

    /**
     * 项目类别
     */
    private String category;

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
     * 支付方式：ALIPAY-支付宝，WECHAT-微信支付
     */
    private String paymentMethod;

    /**
     * 开始时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String startTime;

    /**
     * 结束时间，格式：yyyy-MM-dd HH:mm:ss
     */
    private String endTime;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 10;
}