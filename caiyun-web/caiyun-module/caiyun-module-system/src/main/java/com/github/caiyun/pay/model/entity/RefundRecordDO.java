package com.github.caiyun.pay.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款记录实体
 */
@Data
@TableName("refund_record")
public class RefundRecordDO {

    /**
     * 退款ID
     */
    @TableId
    private String id;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 退款状态：
     * PROCESSING-退款处理中
     * SUCCESS-退款成功
     * FAILED-退款失败
     */
    private String status;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款说明
     */
    private String description;

    /**
     * 退款流水号
     */
    private String refundNo;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 失败原因
     */
    private String failReason;

    /**
     * 预计退款到账时间
     */
    private LocalDateTime expectedArrivalTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Boolean deleted;
}