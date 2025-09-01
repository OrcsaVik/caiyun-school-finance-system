package com.github.caiyun.pay.constant;

/**
 * 支付模块常量类TODO 支付核心
 */
public class PayConstants {

    /**
     * 支付方式
     */
    public static class PaymentMethod {
        /** 支付宝 */
        public static final String ALIPAY = "ALIPAY";
        /** 微信支付 */
        public static final String WECHAT = "WECHAT";
        /** 银联支付 */
        public static final String UNIONPAY = "UNIONPAY";
    }

    /**
     * 支付场景
     */
    public static class PaymentScene {
        /** PC网页支付 */
        public static final String PC = "PC";
        /** 移动网页支付 */
        public static final String H5 = "H5";
        /** APP支付 */
        public static final String APP = "APP";
        /** 小程序支付 */
        public static final String MINI_PROGRAM = "MINI_PROGRAM";
    }

    /**
     * 支付终端
     */
    public static class PaymentTerminal {
        /** PC浏览器 */
        public static final String PC_BROWSER = "PC_BROWSER";
        /** 移动浏览器 */
        public static final String MOBILE_BROWSER = "MOBILE_BROWSER";
        /** 安卓APP */
        public static final String ANDROID_APP = "ANDROID_APP";
        /** iOS APP */
        public static final String IOS_APP = "IOS_APP";
        /** 微信小程序 */
        public static final String WECHAT_MINI_PROGRAM = "WECHAT_MINI_PROGRAM";
    }

    /**
     * 订单状态
     */
    public static class OrderStatus {
        /** 待支付 */
        public static final String PENDING = "PENDING";
        /** 支付中 */
        public static final String PROCESSING = "PROCESSING";
        /** 已支付 */
        public static final String PAID = "PAID";
        /** 已关闭 */
        public static final String CLOSED = "CLOSED";
        /** 已退款 */
        public static final String REFUNDED = "REFUNDED";
    }

    /**
     * 退款状态
     */
    public static class RefundStatus {
        /** 处理中 */
        public static final String PROCESSING = "PROCESSING";
        /** 成功 */
        public static final String SUCCESS = "SUCCESS";
        /** 失败 */
        public static final String FAILED = "FAILED";
    }

    /**
     * 缴费项目状态
     */
    public static class PaymentItemStatus {
        /** 未开始 */
        public static final String NOT_STARTED = "NOT_STARTED";
        /** 进行中 */
        public static final String IN_PROGRESS = "IN_PROGRESS";
        /** 已结束 */
        public static final String ENDED = "ENDED";
    }

    /**
     * 缴费项目类别
     */
    public static class PaymentItemCategory {
        /** 学费 */
        public static final String TUITION = "TUITION";
        /** 住宿费 */
        public static final String ACCOMMODATION = "ACCOMMODATION";
        /** 教材费 */
        public static final String TEXTBOOK = "TEXTBOOK";
        /** 保险费 */
        public static final String INSURANCE = "INSURANCE";
        /** 其他费用 */
        public static final String OTHER = "OTHER";
    }

    /**
     * 订单有效期（分钟）
     */
    public static final int ORDER_EXPIRE_MINUTES = 30;

    /**
     * 最大退款次数
     */
    public static final int MAX_REFUND_TIMES = 3;

    /**
     * 最大退款金额比例（百分比）
     */
    public static final int MAX_REFUND_PERCENTAGE = 100;
}