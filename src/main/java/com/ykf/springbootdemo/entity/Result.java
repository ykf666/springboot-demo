package com.ykf.springbootdemo.entity;

import lombok.Data;

/**
 * @author yan.kefei
 * @date 2018/6/29 22:14
 */
@Data
public class Result<T> {

    private Integer errorCode;

    private String errorMsg;

    private T data;

}
