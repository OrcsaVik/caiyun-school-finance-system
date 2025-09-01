package com.github.caiyun.common.constant;

/**
 * 认证相关常量
 */
public class AuthConstants {

    /**
     * 认证请求头
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Token前缀
     */
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * 用户ID的请求头
     */
    public static final String USER_ID_HEADER = "X-User-ID";

    /**
     * 用户名的请求头
     */
    public static final String USERNAME_HEADER = "X-Username";

    /**
     * 用户角色的请求头
     */
    public static final String USER_ROLE_HEADER = "X-User-Role";

    /**
     * 登录类型 - 密码
     */
    public static final String LOGIN_TYPE_PASSWORD = "password";

    /**
     * 登录类型 - 短信验证码
     */
    public static final String LOGIN_TYPE_SMS = "sms";

    /**
     * 登录类型 - 微信扫码
     */
    public static final String LOGIN_TYPE_WECHAT = "wechat";

    /**
     * 短信验证码有效期（分钟）
     */
    public static final int SMS_CODE_EXPIRE_MINUTES = 5;

    /**
     * 微信扫码登录二维码有效期（分钟）
     */
    public static final int WECHAT_QR_CODE_EXPIRE_MINUTES = 5;

    /**
     * Token 有效期（小时）
     */
    public static final int TOKEN_EXPIRE_HOURS = 24;
}