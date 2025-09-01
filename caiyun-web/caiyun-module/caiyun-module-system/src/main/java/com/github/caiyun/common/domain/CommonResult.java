package com.github.caiyun.common.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用返回结果
 *
 * @param <T> 数据泛型
 */
@NoArgsConstructor
@Data
public class CommonResult<T> implements Serializable {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 错误提示（字段名改为msg，与前端匹配）
     */
    private String msg;

    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = 200;
        result.data = data;
        result.msg = "";
        return result;
    }

    public static <T> CommonResult<T> success() {
        CommonResult<T> result = new CommonResult<>();
        result.code = 200;
        result.msg = "";
        return result;
    }

    public static <T> CommonResult<T> error(Integer code, String message) {
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.msg = message;
        return result;
    }

    public static <T> CommonResult<T> error(Integer code, String message, T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = code;
        result.data = data;
        result.msg = message;
        return result;
    }
}