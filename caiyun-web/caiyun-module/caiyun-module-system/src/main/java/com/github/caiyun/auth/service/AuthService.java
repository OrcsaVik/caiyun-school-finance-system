package com.github.caiyun.auth.service;

import com.github.caiyun.auth.model.vo.user.*;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 账号密码登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponseVO loginByPassword(PasswordLoginRequestVO request);

    /**
     * 短信验证码登录
     *
     * @param request 登录请求
     * @return 登录响应
     */
    LoginResponseVO loginBySms(SmsLoginRequestVO request);

    /**
     * 发送短信验证码
     *
     * @param request 发送验证码请求
     */
    void sendSmsCode(SendSmsCodeRequestVO request);

    /**
     * 生成微信登录二维码
     *
     * @return 二维码信息
     */
    WechatQrCodeVO generateWechatQrCode();

    /**
     * 检查微信扫码状态
     *
     * @param qrCodeId 二维码ID
     * @return 扫码状态
     */
    WechatLoginStatusVO checkWechatLoginStatus(String qrCodeId);

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    UserInfoVO getCurrentUserInfo();

    /**
     * 退出登录
     */
    void logout();
}
