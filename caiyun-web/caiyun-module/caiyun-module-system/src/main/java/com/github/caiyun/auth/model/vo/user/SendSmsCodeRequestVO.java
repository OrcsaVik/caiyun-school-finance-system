package com.github.caiyun.auth.model.vo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 发送短信验证码请求
 */
@Data
public class SendSmsCodeRequestVO {

    /**
     * 手机号
     */
//    @NotBlank(message = "手机号不能为空")
//    @PhoneNumber(message = "手机格式错误")
    private String phone;

    /**
     * 短信验证码用途
     * - login: 登录
     * - register: 注册
     * - resetPassword: 重置密码
     */
    @NotBlank(message = "验证码用途不能为空")
    @Pattern(regexp = "^(login|register|resetPassword)$", message = "验证码用途不正确")
    private String usage;
}