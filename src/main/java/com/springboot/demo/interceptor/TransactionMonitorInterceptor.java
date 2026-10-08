package com.springboot.demo.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务响应码校验拦截器
 * 在业务逻辑执行完成后，读取响应体第一层的 code 字段：
 * code == 0 表示成功，code != 0 表示失败，并记录对应的 msg 信息。
 * <p>
 * 实现方式：preHandle 阶段用 {@link ContentCachingResponseWrapper} 缓存响应体，
 * afterCompletion 阶段读取缓存内容做业务码判断，最后回写响应。
 */
@Component
@Slf4j
public class TransactionMonitorInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();
    private static final ThreadLocal<String> REQUEST_URI = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> LOG_WRITTEN = new ThreadLocal<>();
    private static final ThreadLocal<String> REQUEST_TIME = new ThreadLocal<>();

    private final ObjectMapper objectMapper;

    public TransactionMonitorInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String formattedTime = LocalDateTime.now().format(formatter);
        START_TIME.set(startTime);
        REQUEST_URI.set(request.getRequestURI());
        LOG_WRITTEN.set(false);
        REQUEST_TIME.set(formattedTime);
        log.debug("请求开始，URI：{}，Method：{}", request.getRequestURI(), request.getMethod());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            Map<String, Object> resultMap = null;
            ContentCachingResponseWrapper cachedResponse = new ContentCachingResponseWrapper(response);
            String contentType = cachedResponse.getContentType();
            if (contentType != null && contentType.toLowerCase().startsWith("application/json")) {
                String responseBody = new String(cachedResponse.getContentAsByteArray());
                if (StringUtils.isNotBlank(responseBody)) {
                    resultMap = objectMapper.readValue(responseBody, HashMap.class);
                }
            } else {
                resultMap = new HashMap<>();
                resultMap.put("code", 0);
                resultMap.put("msg", "成功");
            }

            if (resultMap != null && (Integer) resultMap.get("code") == 0) {
                log.info("请求监控成功");
            }
        } catch (Exception e) {
            log.error("监控异常", e);
        } finally {
            //清空threadlocal
        }

    }
}