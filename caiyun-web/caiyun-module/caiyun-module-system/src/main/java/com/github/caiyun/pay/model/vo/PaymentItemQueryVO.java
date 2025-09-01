package com.github.caiyun.pay.model.vo;

import lombok.Data;

/**
 * 缴费项目查询条件
 */
@Data
public class PaymentItemQueryVO {

    /**
     * 项目名称，模糊匹配
     */
    private String name;

    /**
     * 项目类别
     */
    private String category;

    /**
     * 缴费状态
     */
    private String status;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 10;
}