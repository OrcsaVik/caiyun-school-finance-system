package com.github.caiyun.auth.model.vo.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 密码登录请求
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordLoginRequestVO {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}