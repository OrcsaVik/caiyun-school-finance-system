package com.github.caiyun.auth.model.vo.user;

import lombok.Data;

/**
 * 登录响应返回类
 */
@Data
public class LoginResponseVO {

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 用户信息
     */
    private UserInfoVO userInfo;

    /**
     * 创建登录响应
     */
    public static LoginResponseVO tokenOf(String accessToken, UserInfoVO userInfo) {
        LoginResponseVO response = new LoginResponseVO();
        response.setAccessToken(accessToken);
        response.setUserInfo(userInfo);
        return response;
    }
}