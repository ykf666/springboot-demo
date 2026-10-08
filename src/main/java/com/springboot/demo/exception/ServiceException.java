package com.springboot.demo.exception;

/**
 * @author yan.kefei
 * @date 2018/7/4 23:27
 */
public class ServiceException extends RuntimeException {

    private Integer errorCode;

    public ServiceException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
