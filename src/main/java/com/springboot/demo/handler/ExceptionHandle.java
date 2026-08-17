package com.springboot.demo.handler;

import com.springboot.demo.entity.Result;
import com.springboot.demo.exception.PersonException;
import com.springboot.demo.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yan.kefei
 * 统一异常处理
 * @date 2018/7/4 23:15
 */
@ControllerAdvice
public class ExceptionHandle {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e){
        logger.error("异常：", e);
        if (e instanceof PersonException) {
            PersonException personException = (PersonException) e;
            return ResultUtil.error(personException.getErrCode(), e.getMessage());
        } else {
            return ResultUtil.error(-1, "未知错误");
        }
    }
}
