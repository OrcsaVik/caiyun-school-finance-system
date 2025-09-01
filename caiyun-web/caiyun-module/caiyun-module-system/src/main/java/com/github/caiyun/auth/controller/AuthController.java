package com.github.caiyun.auth.controller;


import com.github.caiyun.auth.model.vo.user.*;
import com.github.caiyun.auth.service.AuthService;
import com.github.caiyun.common.domain.CommonResult;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * 账号密码登录
     */
    @PostMapping("/login/password")
    public CommonResult<LoginResponseVO> loginByPassword(@RequestBody @Valid PasswordLoginRequestVO request) {
        return CommonResult.success(authService.loginByPassword(request));
    }

    /**
     * 短信验证码登录
     */
    @PostMapping("/login/sms")
    public CommonResult<LoginResponseVO> loginBySms(@RequestBody @Valid SmsLoginRequestVO request) {
        return CommonResult.success(authService.loginBySms(request));
    }

    /**
     * 发送短信验证码
     */
    @PostMapping("/sms/send")
    public CommonResult<Void> sendSmsCode(@RequestBody @Valid SendSmsCodeRequestVO request) {
        authService.sendSmsCode(request);
        return CommonResult.success();
    }

    /**
     * 获取微信登录二维码
     */
    @GetMapping("/wechat/qrcode")
    public CommonResult<WechatQrCodeVO> getWechatQrCode() {
        return CommonResult.success(authService.generateWechatQrCode());
    }

    /**
     * 检查微信扫码状态
     * 当前项目默认将状态设置为确认
     *
     * 调用该接口
     */
    @GetMapping("/wechat/check")
    public CommonResult<WechatLoginStatusVO> checkWechatLoginStatus(@RequestParam String qrCodeId) {
        return CommonResult.success(authService.checkWechatLoginStatus(qrCodeId));
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/user/info")
    public CommonResult<UserInfoVO> getCurrentUserInfo() {
        return CommonResult.success(authService.getCurrentUserInfo());
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public CommonResult<Void> logout() {
        authService.logout();
        return CommonResult.success();
    }
}
