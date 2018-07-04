package com.ykf.springbootdemo.utils;

import com.ykf.springbootdemo.entity.Result;

/**
 * @author yan.kefei
 * @date 2018/6/29 22:24
 */
public class ResultUtil {

    public static Result success(Object object){
        Result result = new Result();
        result.setErrorCode(0);
        result.setErrorMsg("成功");
        result.setData(object);
        return result;
    }

    public static Result error(int errorCode, String errorMsg){
        Result result = new Result();
        result.setErrorCode(errorCode);
        result.setErrorMsg(errorMsg);
        result.setData(null);
        return result;
    }

}
