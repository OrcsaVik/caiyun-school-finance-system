package com.github.caiyun.auth.model.vo.user;

import lombok.Data;

/**
 * 微信登录状态
 */
@Data
public class WechatLoginStatusVO {

    /**
     * 扫码状态
     * - WAITING: 等待扫码
     * - SCANNED: 已扫码，等待确认
     * - CONFIRMED: 已确认登录
     * - CANCELED: 已取消登录
     * - EXPIRED: 二维码已过期
     */
    private String status;

    /**
     * 登录成功后的令牌，仅在status=CONFIRMED时返回
     */
    private String accessToken;

    /**
     * 登录成功后的用户信息，仅在status=CONFIRMED时返回
     */
    private UserInfoVO userInfo;
}