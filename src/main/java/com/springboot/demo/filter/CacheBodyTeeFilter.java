package com.springboot.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求体缓存过滤器
 * 使用ContentCachingRequestWrapper缓存JSON类型的请求体，
 * 以便后续过滤器或拦截器可以多次读取请求体内容。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CacheBodyTeeFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (isJsonRequest(servletRequest)) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            //异步下载解决方案二：使用双写response流
            CachingResponseWrapper wrappedResponse = new CachingResponseWrapper(response);
            try {
                filterChain.doFilter(wrappedRequest, wrappedResponse);
            } finally {
                if (!wrappedResponse.isCommitted()) {
                    wrappedResponse.copyBodyToResponse();
                }
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 判断请求是否为JSON类型
     */
    private boolean isJsonRequest(ServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE);
    }
}
