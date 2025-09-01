package com.github.caiyun.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import com.github.caiyun.auth.constant.RedisKeyConstants;
import com.github.caiyun.auth.domain.dataobject.UserDO;
import com.github.caiyun.mapper.UserDOMapper;
import com.github.caiyun.auth.enums.ResponseCodeEnum;
import com.github.caiyun.auth.model.vo.user.*;
import com.github.caiyun.auth.service.AuthService;
import com.github.caiyun.auth.sms.AliyunSmsHelper;
import com.github.caiyun.common.constant.AuthConstants;
import com.github.caiyun.common.exception.ServiceException;
import com.github.framework.common.exception.BizException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Resource
    private UserDOMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private AliyunSmsHelper smsHelper;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public LoginResponseVO loginByPassword(PasswordLoginRequestVO request) {
        // 查询用户
        UserDO user = userMapper.selectByAccount(request.getAccount());
        if (Objects.isNull(user)) {
            throw new ServiceException(401, "用户名或密码错误");
        }

        String password = user.getPassword();

        if(!password.equals(request.getPassword())){
            throw new ServiceException(401, "用户名或密码错误");
        }

        // 校验密码 不使用加密密码 TODO
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new ServiceException(401, "用户名或密码错误");
//        }

        // 生成令牌并登录
        return createLoginResponse(user);
    }

    @Override
    public LoginResponseVO loginBySms(SmsLoginRequestVO request) {
        // 校验验证码
        String codeKey = RedisKeyConstants.buildVerificationCodeKey(request.getPhone());
        String cachedCode = (String) redisTemplate.opsForValue().get(codeKey);
        if(Objects.isNull(cachedCode)){
            throw new ServiceException(401, "<验证码过期 请重新发送~>");
        }
        if (!cachedCode.equals(request.getCode())) {

            throw new ServiceException(401, "验证码错误");
        }

        // 删除验证码 后续可以设置对应过期时间 而不是手动删除
        redisTemplate.delete(codeKey);

        // 查询或注册用户
        UserDO user = userMapper.selectByPhone(request.getPhone());
        if (Objects.isNull(user)) {
            // 手机号首次登录，自动注册
            user = new UserDO();
            user.setPhone(request.getPhone());
            user.setUsername("用户id"+ RandomStringUtils.randomAlphanumeric(8));
            user.setPassword("123456");

            userMapper.insert(user);
        }

        // 生成令牌并登录
        return createLoginResponse(user);
    }

    @Override
    public void sendSmsCode(SendSmsCodeRequestVO request) {
        // 手机号
        String phone = request.getPhone();

        // 构建验证码 redis key
        String key = RedisKeyConstants.buildVerificationCodeKey(phone);

        // 判断是否已发送验证码
        boolean isSent = redisTemplate.hasKey(key);
        if (isSent) {
            // 若之前发送的验证码未过期，则提示发送频繁
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

        // 生成 6 位随机数字验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        log.info("==> 手机号: {}, 已生成验证码：【{}】", phone, verificationCode);

        //调用第三方短信发送服务 实际对应的短信服务
        threadPoolTaskExecutor.submit(() -> {
            String signName = "阿里云短信测试";
            String templateCode = "SMS_154950909";
            String templateParam = String.format("{\"code\":\"%s\"}", verificationCode);
            smsHelper.sendMessage(signName, templateCode, phone, templateParam);
        });


        // 存储验证码到 redis, 并设置过期时间为 3 分钟
        redisTemplate.opsForValue().set(key, verificationCode, AuthConstants.SMS_CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
    }


    /**
     * 死实现
     * @return
     */
    @Override
    public WechatQrCodeVO generateWechatQrCode() {
        // 生成二维码ID
        String qrCodeId = UUID.randomUUID().toString();

        // TODO: 调用微信开放平台API生成二维码
        String qrCodeUrl = "data:image/png;base64,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";

        // 保存二维码状态到Redis
        String qrCodeKey = RedisKeyConstants.WECHAT_QR_CODE_KEY + qrCodeId;
        redisTemplate.opsForValue().set(qrCodeKey, "WAITING", AuthConstants.WECHAT_QR_CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);

        // 返回二维码信息
        WechatQrCodeVO response = new WechatQrCodeVO();
        response.setQrCodeId(qrCodeId);
        response.setQrCodeUrl(qrCodeUrl);
        response.setExpireSeconds(AuthConstants.WECHAT_QR_CODE_EXPIRE_MINUTES * 60);
        return response;
    }

    @Override
    public WechatLoginStatusVO checkWechatLoginStatus(String qrCodeId) {
        // 获取二维码状态
        String qrCodeKey = RedisKeyConstants.WECHAT_QR_CODE_KEY + qrCodeId;
        String status = (String) redisTemplate.opsForValue().get(qrCodeKey);



        WechatLoginStatusVO response = new WechatLoginStatusVO();
        if (status == null) {
            response.setStatus("EXPIRED");
            return response;
        }

        response.setStatus("CONFIRMED");

        // 如果已确认登录，返回令牌和用户信息 空实现
//        if ("CONFIRMED".equals(status)) {
//            String userIdKey = RedisKeyConstants.WECHAT_QR_CODE_USER_KEY + qrCodeId;
//            String userId = (String) redisTemplate.opsForValue().get(userIdKey);
//            if (userId != null) {
//                UserDO user = userMapper.selectById(userId);
//                if (user != null) {
//                    LoginResponseVO loginResponse = createLoginResponse(user);
//                    response.setAccessToken(loginResponse.getAccessToken());
//                    response.setUserInfo(loginResponse.getUserInfo());
//                }
//            }
//        }

        return response;
    }

    @Override
    //解析对应token
    public UserInfoVO getCurrentUserInfo() {
        // 获取当前登录用户ID
        String userId = StpUtil.getLoginIdAsString();
        if (userId == null) {
            throw new ServiceException(401, "用户未登录");
        }

        // 查询用户信息
        UserDO user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServiceException(401, "用户不存在");
        }

        return convertToUserInfo(user);
    }

    @Override
    public void logout() {
        StpUtil.logout();
    }

    /**
     * 创建登录响应
     */
    private LoginResponseVO createLoginResponse(UserDO user) {
        // 生成令牌
        StpUtil.login(user.getId());
        String token = StpUtil.getTokenValue();

        // 转换用户信息
        UserInfoVO userInfo = convertToUserInfo(user);

        return LoginResponseVO.tokenOf(token, userInfo);
    }

    /**
     * 转换为用户信息VO TODO 错误
     * UserInfoVO userInfo = new UserInfoVO();
     *         userInfo.setUserId(user.getId());
     *         userInfo.setUsername(user.getUsername());
     *         userInfo.setNickname(user.getNickname());
     *         userInfo.setMobile(user.getMobile());
     *         userInfo.setEmail(user.getEmail());
     *         userInfo.setAvatar(user.getAvatar());
     *         userInfo.setRole(user.getRole());
     *         userInfo.setStudentOrTeacherNumber(user.getStudentOrTeacherNumber());
     *         userInfo.setDepartment(user.getDepartment());
     *         userInfo.setClassName(user.getClassName());
     *         userInfo.setWechatBound(StringUtils.isNotBlank(user.getWechatOpenId()));
     *         return userInfo;
     */
    //使用死数据
    private UserInfoVO convertToUserInfo(UserDO user) {
//        UserInfoVO userInfo = new UserInfoVO();
        UserInfoVO userInfo = UserInfoVO.builder()
                .userId(user.getId() != null ? String.valueOf(user.getId()) : UUID.randomUUID().toString())
                .username(StringUtils.defaultString(user.getUsername()))
                .nickname(StringUtils.defaultString("ADMIN"))
                .mobile(StringUtils.defaultString(user.getPhone()))
                .email(StringUtils.defaultString(user.getEmail()))
                .avatar(StringUtils.defaultString(user.getAvatar(), "https://example.com/default-avatar.png"))
                .role(StringUtils.defaultString("student"))
                .account(StringUtils.defaultString(user.getAccount()))
                .department(StringUtils.defaultString("法学院"))
                .className(StringUtils.defaultString( "法学100班"))
                .wechatBound(Boolean.FALSE).build();
        return userInfo;
    }
}
