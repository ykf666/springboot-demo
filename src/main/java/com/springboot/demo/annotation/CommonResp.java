package com.springboot.demo.annotation;

import java.lang.annotation.*;

/**
 * 通用响应包装注解
 * 标注在Controller类或方法上，自动将返回值包装为ApiResponse格式
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommonResp {
}
