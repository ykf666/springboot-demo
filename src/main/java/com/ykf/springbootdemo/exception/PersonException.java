package com.ykf.springbootdemo.exception;

/**
 * @author yan.kefei
 * @date 2018/7/4 23:27
 */
public class PersonException extends RuntimeException {

    private Integer errCode;

    public PersonException(Integer errCode, String errMsg){
        super(errMsg);
        this.errCode = errCode;
    }

    public Integer getErrCode() {
        return errCode;
    }
}
