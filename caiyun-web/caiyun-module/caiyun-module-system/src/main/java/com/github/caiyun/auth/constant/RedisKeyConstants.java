package com.github.caiyun.auth.constant;


public class RedisKeyConstants {

    /**
     * 验证码 KEY 前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX = "verification_code:";

    /**
     *
     *
     */
    public static final String USER_ID_GENERATOR_KEY = "user_id_generator";

    /**
     * 用户角色数据 KEY 前缀
     */
    private static final String USER_ROLES_KEY_PREFIX = "user:roles:";

    /**
     * 角色对应的权限集合 KEY 前缀
     */
    private static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permissions:";


    public static final String WECHAT_QR_CODE_KEY = "wechat_qr_code:";

    /**
     * 构建验证码 KEY
     * @param phone
     * @return
     */
    public static String buildVerificationCodeKey(String phone) {
        return VERIFICATION_CODE_KEY_PREFIX + phone;
    }

    /**
     * 构建验证码 KEY
     * @param
     * @return
     */
    public static String buildUserRoleKey(Long userId ) {
        return USER_ROLES_KEY_PREFIX + userId;
    }

    /**
     * 构建角色对应的权限集合 KEY
     * @param roleRedisKey
     * @return
     */
    public static String buildRolePermissionsKey(String roleRedisKey) {
        return ROLE_PERMISSIONS_KEY_PREFIX + roleRedisKey;
    }


}
