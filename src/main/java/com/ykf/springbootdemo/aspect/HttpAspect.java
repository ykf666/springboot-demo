package com.ykf.springbootdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yan.kefei
 * @date 2018/6/26 23:19
 */
@Aspect
@Component
public class HttpAspect {

    private static final Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(* com.ykf.springbootdemo.controller.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinpoint) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        //url
        logger.info("url: {}", request.getRequestURL().toString());
        //method
        logger.info("method: {}", request.getMethod());
        //ip
        logger.info("ip: {}", request.getRemoteAddr());
        //类方法
        logger.info("class_method: {}", joinpoint.getSignature().getDeclaringTypeName() + "." + joinpoint.getSignature().getName());
        //参数
        logger.info("params: {}", joinpoint.getArgs());
    }

    @After("log()")
    public void doAfter() {
        logger.info("do after......");
    }

    @AfterReturning(returning = "object", pointcut = "log()")
    public void doAfterReturning(Object object){
        logger.info("response: {}", object);
    }
}
