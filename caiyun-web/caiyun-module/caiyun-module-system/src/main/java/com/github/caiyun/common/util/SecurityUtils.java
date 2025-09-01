package com.github.caiyun.common.util;


import com.github.caiyun.common.constant.AuthConstants;
import com.github.caiyun.common.exception.ServiceException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 安全服务工具类
 */
public class SecurityUtils {

    /**
     * 获取当前用户ID
     */
    public static String getUserId() {
        String userId = getHeader(AuthConstants.USER_ID_HEADER);
        if (userId == null) {
            throw new ServiceException(401, "用户未登录");
        }
        return userId;
    }

    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        String username = getHeader(AuthConstants.USERNAME_HEADER);
        if (username == null) {
            throw new ServiceException(401, "用户未登录");
        }
        return username;
    }

    /**
     * 获取当前用户角色
     */
    public static String getUserRole() {
        String role = getHeader(AuthConstants.USER_ROLE_HEADER);
        if (role == null) {
            throw new ServiceException(401, "用户未登录");
        }
        return role;
    }

    /**
     * 获取请求token
     */
    public static String getToken() {
        String token = getHeader(AuthConstants.AUTHORIZATION_HEADER);
        if (token != null && token.startsWith(AuthConstants.BEARER_PREFIX)) {
            token = token.substring(AuthConstants.BEARER_PREFIX.length());
        }
        return token;
    }

    /**
     * 获取请求头信息
     */
    public static String getHeader(String header) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            String header1 = ((ServletRequestAttributes) requestAttributes).getRequest().getHeader(header);
            return header1;
        }
        return null;
    }

    /**
     * 是否为管理员
     */
    public static boolean isAdmin() {
        return "admin".equals(getUserRole());
    }
}