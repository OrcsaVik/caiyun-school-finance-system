package com.github.caiyun.auth.model.vo.user;

import lombok.Data;

/**
 * 微信登录二维码
 */
@Data
public class WechatQrCodeVO {

    /**
     * 二维码ID，用于后续查询扫码状态
     */
    private String qrCodeId;

    /**
     * 二维码图片URL
     */
    private String qrCodeUrl;

    /**
     * 二维码有效期（秒）
     */
    private Integer expireSeconds;
}