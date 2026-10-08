package com.springboot.demo.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.demo.annotation.CommonResp;
import com.springboot.demo.dto.CommonApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

/**
 * 响应体包装处理
 *
 * @author kefei.yan
 * @date 2026/9/30
 */
@Order(0)
@RestControllerAdvice
@Slf4j
public class CommonRespAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public CommonRespAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 已经是ApiResponse类型的不再包装
        if (CommonApiResponse.class.isAssignableFrom(returnType.getParameterType())) {
            return false;
        }
        // HttpEntity/ResponseEntity/StreamingResponseBody类型不包装（文件下载等二进制流场景）
        Class<?> returnTypeClass = returnType.getParameterType();
        if (HttpEntity.class.isAssignableFrom(returnTypeClass)
                || ResponseEntity.class.isAssignableFrom(returnTypeClass)
                || StreamingResponseBody.class.isAssignableFrom(returnTypeClass)) {
            return false;
        }
        // 检查方法或类上是否有@CommonResp注解
        return returnType.hasMethodAnnotation(CommonResp.class)
                || (returnType.getMethod() != null
                && returnType.getMethod().getDeclaringClass().isAnnotationPresent(CommonResp.class));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (returnType.getParameterType().getName().equals("void")) {
            return CommonApiResponse.success();
        }
        if (body instanceof CommonApiResponse) {
            return body;
        }
        // String类型需要特殊处理，手动序列化
        if (body instanceof String) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                CommonApiResponse<Object> commonApiResponse = CommonApiResponse.success(body);
                return objectMapper.writeValueAsString(commonApiResponse);
            } catch (Exception e) {
                log.error("响应包装序列化失败", e);
                return body;
            }
        }
        return CommonApiResponse.success(body);
    }
}