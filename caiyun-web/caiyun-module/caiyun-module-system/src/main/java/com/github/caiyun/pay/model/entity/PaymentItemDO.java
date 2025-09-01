package com.github.caiyun.pay.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缴费项目实体
 */
@Data
@TableName("payment_item")
public class PaymentItemDO {

    /**
     * 项目ID
     */
    @TableId
    private String id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目类别：学费、住宿费、书本费、其他费用
     */
    private String category;

    /**
     * 缴费金额
     */
    private BigDecimal amount;

    /**
     * 减免金额
     */
    private BigDecimal exemptAmount;

    /**
     * 实际应缴金额
     */
    private BigDecimal actualAmount;

    /**
     * 缴费状态：
     * DRAFT-草稿
     * PUBLISHED-已发布
     * CANCELLED-已取消
     * EXPIRED-已过期
     * todo 结合创建支付状态
     */
    private String status;

    /**
     * 缴费开始时间
     */
    private LocalDateTime startTime;

    /**
     * 缴费截止时间
     */
    private LocalDateTime endTime;

    /**
     * 缴费说明
     */
    private String description;

    /**
     * 缴费说明文档URL
     */
    private String documentUrl;

    /**
     * 缴费须知，JSON数组格式
     */
    private String notices;

    /**
     * 缴费流程，JSON数组格式
     */
    private String processes;

    /**
     * 常见问题，JSON数组格式
     */
    private String faqs;

    /**
     * 联系人信息，JSON数组格式
     */
    private String contacts;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Boolean deleted;
}