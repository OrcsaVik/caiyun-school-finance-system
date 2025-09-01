package com.github.caiyun.pay.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 缴费项目详情
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentItemDetailVO extends PaymentItemVO {

    /**
     * 缴费说明文档URL
     */
    private String documentUrl;

    /**
     * 缴费须知
     */
    private List<String> notices;

    /**
     * 缴费流程
     */
    private List<String> processes;

    /**
     * 常见问题
     */
    private List<FaqVO> faqs;

    /**
     * 联系人信息
     */
    private List<ContactVO> contacts2;

    /**
     * 常见问题
     */
    @Data
    public static class FaqVO {
        /**
         * 问题
         */
        private String question;

        /**
         * 答案
         */
        private String answer;
    }

    /**
     * 联系人信息
     */
    @Data
    public static class ContactVO {
        /**
         * 联系人姓名
         */
        private String name;

        /**
         * 联系人部门
         */
        private String department;

        /**
         * 联系电话
         */
        private String phone;

        /**
         * 电子邮箱
         */
        private String email;

        /**
         * 办公地点
         */
        private String office;
    }
}