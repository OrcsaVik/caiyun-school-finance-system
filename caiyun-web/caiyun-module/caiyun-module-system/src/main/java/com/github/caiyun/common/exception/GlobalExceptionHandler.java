package com.github.caiyun.common.exception;

import com.github.caiyun.common.domain.CommonResult;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理所有异常，主要是处理未知异常
     */
    @ExceptionHandler(Exception.class)
    public CommonResult<?> handleException(Exception ex) {
        log.error("[handleException]", ex);
        return CommonResult.error(500, ex.getMessage());
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(value = ServiceException.class)
    public CommonResult<?> handleServiceException(ServiceException ex) {
        log.error("[handleServiceException]", ex);
        return CommonResult.error(ex.getCode(), ex.getMessage());
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public CommonResult<?> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("[handleConstraintViolationException]", ex);
        StringBuilder message = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            message.append(violation.getMessage()).append("\n");
        }
        return CommonResult.error(400, message.toString());
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public CommonResult<?> handleBindException(BindException ex) {
        log.warn("[handleBindException]", ex);
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : ex.getFieldErrors()) {
            message.append(fieldError.getDefaultMessage()).append("\n");
        }
        return CommonResult.error(400, message.toString());
    }

    /**
     * 处理请求参数缺失异常
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public CommonResult<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn("[handleMissingServletRequestParameterException]", ex);
        return CommonResult.error(400, "请求参数缺失: " + ex.getParameterName());
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public CommonResult<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("[handleMethodArgumentTypeMismatchException]", ex);
        return CommonResult.error(400, "参数类型不匹配: " + ex.getMessage());
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public CommonResult<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("[handleHttpRequestMethodNotSupportedException]", ex);
        return CommonResult.error(405, "请求方法不支持: " + ex.getMessage());
    }

    /**
     * 处理 JSON 参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("[handleMethodArgumentNotValidException]", ex);
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message.append(fieldError.getDefaultMessage()).append("\n");
        }
        return CommonResult.error(400, message.toString());
    }
}