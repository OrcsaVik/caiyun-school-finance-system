package com.github.caiyun.common.constant;

/**
 * 支付相关常量
 */
public class PaymentConstants {

    /**
     * 支付项目状态 - 未缴费
     */
    public static final String STATUS_PENDING = "未缴费";

    /**
     * 支付项目状态 - 已缴费
     */
    public static final String STATUS_PAID = "已缴费";

    /**
     * 支付项目状态 - 已逾期
     */
    public static final String STATUS_OVERDUE = "已逾期";

    /**
     * 支付项目状态 - 部分减免
     */
    public static final String STATUS_PARTIAL_EXEMPT = "部分减免";

    /**
     * 支付订单状态 - 待支付
     */
    public static final String ORDER_STATUS_PENDING = "pending";

    /**
     * 支付订单状态 - 支付成功
     */
    public static final String ORDER_STATUS_SUCCESS = "success";

    /**
     * 支付订单状态 - 支付失败
     */
    public static final String ORDER_STATUS_FAILED = "failed";

    /**
     * 退款状态 - 待处理
     */
    public static final String REFUND_STATUS_PENDING = "pending";

    /**
     * 退款状态 - 已批准
     */
    public static final String REFUND_STATUS_APPROVED = "approved";

    /**
     * 退款状态 - 已拒绝
     */
    public static final String REFUND_STATUS_REJECTED = "rejected";

    /**
     * 退款状态 - 已完成
     */
    public static final String REFUND_STATUS_COMPLETED = "completed";

    /**
     * 支付项目类别 - 学费
     */
    public static final String CATEGORY_TUITION = "学费";

    /**
     * 支付项目类别 - 住宿费
     */
    public static final String CATEGORY_ACCOMMODATION = "住宿费";

    /**
     * 支付项目类别 - 书本费
     */
    public static final String CATEGORY_BOOKS = "书本费";

    /**
     * 支付项目类别 - 其他费用
     */
    public static final String CATEGORY_OTHERS = "其他费用";

    /**
     * 支付订单有效期（分钟）
     */
    public static final int ORDER_EXPIRE_MINUTES = 30;
}