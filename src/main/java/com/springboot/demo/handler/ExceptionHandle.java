package com.springboot.demo.handler;

import com.springboot.demo.dto.CommonApiResponse;
import com.springboot.demo.exception.ServiceException;
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

    @ExceptionHandler(value = ServiceException.class)
    @ResponseBody
    public CommonApiResponse<String> handle(ServiceException e) {
        logger.error("服务异常：", e);
        return CommonApiResponse.error(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public CommonApiResponse<String> handle(Exception e) {
        logger.error("未知异常：", e);
        return CommonApiResponse.error(-1, "系统异常，请稍后重试");
    }
}
