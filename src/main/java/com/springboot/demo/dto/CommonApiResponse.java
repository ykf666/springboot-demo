package com.springboot.demo.dto;

import lombok.Data;

/**
 * 统一业务响应格式
 * code=0 表示成功，data为返回内容，msg为异常信息或"成功"
 */
@Data
public class CommonApiResponse<T> {

    private int code;

    private T data;

    private String msg;

    public static <T> CommonApiResponse<T> success() {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.setCode(0);
        response.setData(null);
        response.setMsg("成功");
        return response;
    }

    public static <T> CommonApiResponse<T> success(T data) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.setCode(0);
        response.setData(data);
        response.setMsg("成功");
        return response;
    }

    public static <T> CommonApiResponse<T> error(int code, String msg) {
        CommonApiResponse<T> response = new CommonApiResponse<>();
        response.setCode(code);
        response.setData(null);
        response.setMsg(msg);
        return response;
    }
}
