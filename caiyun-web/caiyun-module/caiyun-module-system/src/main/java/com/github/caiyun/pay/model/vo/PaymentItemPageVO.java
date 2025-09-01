package com.github.caiyun.pay.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 缴费项目分页响应
 */
@Data
public class PaymentItemPageVO {

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
     * 缴费项目列表
     */
    private List<PaymentItemVO> list;

    /**
     * 创建分页响应
     */
    public static PaymentItemPageVO of(List<PaymentItemVO> list, Long total, Integer pageNum, Integer pageSize) {
        PaymentItemPageVO page = new PaymentItemPageVO();
        page.setList(list);
        page.setTotal(total);
        page.setPageNum(pageNum);
        page.setPageSize(pageSize);
        page.setPages((int) Math.ceil((double) total / pageSize));
        return page;
    }
}