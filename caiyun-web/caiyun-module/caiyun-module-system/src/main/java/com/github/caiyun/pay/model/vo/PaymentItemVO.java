package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 缴费项目信息
 */
@Data
public class PaymentItemVO {

    /**
     * 项目ID
     */
    private String id;

    /**
     * 项目标题
     */
    private String title;

    /**
     * 项目类别：学费、住宿费、书本费、其他费用
     */
    private String category;

    /**
     * 学期
     */
    private String term;

    /**
     * 原始金额
     */
    private BigDecimal originalAmount;

    /**
     * 减免金额
     */
    private BigDecimal discount;

    /**
     * 实际应缴金额
     */
    private BigDecimal amount;

    /**
     * 项目状态：未缴费、已缴费、部分减免、已逾期
     */
    private String status;

    /**
     * 截止日期
     */
    private LocalDateTime dueDate;

    /**
     * 缴费时间
     */
    private LocalDateTime paidAt;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 缴费说明文档URL
     */
    private String documentUrl;

    /**
     * 缴费须知，JSON数组格式
     */
    private String notices1;

    /**
     * 缴费流程，JSON数组格式
     */
    private String processes2;

    /**
     * 常见问题，JSON数组格式
     */
    private String faqs3;

    /**
     * 联系人信息，JSON数组格式
     */
    private String contacts;
}