package com.github.framework.common.utils;

import java.util.regex.Pattern;

public final class ParamUtils {
    private ParamUtils() {
    }

    // ============================== 校验昵称 ==============================
    // 定义姓名长度范围
    private static final int NICK_NAME_MIN_LENGTH = 1;
    private static final int NICK_NAME_MAX_LENGTH = 18;

    // 定义特殊字符的正则表达式
    private static final String NICK_NAME_REGEX = "[!@#$%^&*(),.?\":{}|<>]";

    /**
     * 昵称校验
     *
     * @param nickname
     * @return
     */
    public static boolean checkNickname(String nickname) {
        // 检查长度
        if (nickname.length() < NICK_NAME_MIN_LENGTH || nickname.length() > NICK_NAME_MAX_LENGTH) {
            return false;
        }

        // 检查是否含有特殊字符
        Pattern pattern = Pattern.compile(NICK_NAME_REGEX);
        return !pattern.matcher(nickname).find();
    }

    // ============================== 学号检验处理 ==============================
    // 定义 ID 长度范围
    private static final int ID_LENGTH = 11;

    // 定义正则表达式
    private static final String ID_REGEX = "^[0-9]+$";

    /**
     * 学号进行检验 TODO
     *
     * @param studentId
     * @return
     */
    public static boolean checkStudentIds(String studentId) {
        // 检查长度
        if(studentId.length()  != ID_LENGTH) {
            return false;
        }
        // 检查格式
        Pattern pattern = Pattern.compile(ID_REGEX);
        return pattern.matcher(studentId).matches();
    }

    /**
     * 字符串长度校验
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean checkLength(String str, int length) {
        // 检查长度
        if (str.isEmpty() || str.length() > length) {
            return false;
        }
        return true;
    }
}