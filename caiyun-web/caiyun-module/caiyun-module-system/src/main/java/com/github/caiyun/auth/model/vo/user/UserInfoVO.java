package com.github.caiyun.auth.model.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息 返回TODO 存在对应信息对应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoVO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 角色
     * - admin: 管理员
     * - student: 学生
     * - teacher: 教师
     */
    private String role;

    /**
     * 学号/工号 账号
     */
    private String account;

    /**
     * 院系
     */
    private String department;

    /**
     * 班级
     */
    private String className;

    /**
     * 是否已绑定微信
     */
    private Boolean wechatBound;
}