package com.springboot.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

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
public class CacheBodyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (isJsonRequest(servletRequest)) {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
            try {
                filterChain.doFilter(wrappedRequest, wrappedResponse);
            } finally {
//                if (!response.isCommitted()) {
//                    wrappedResponse.copyBodyToResponse();
//                }
                //异步下载解决方案一：判断异步并设置监听器
                if (!wrappedResponse.isCommitted()) {
                    //判断是否异步：异步下载时，在finally里拷贝必然拿到空body
                    if (wrappedRequest.isAsyncStarted()) {
                        // 交给Listener 在异步写完后再拷贝
                        wrappedRequest.getAsyncContext().addListener(new AsyncListener() {
                            @Override
                            public void onComplete(AsyncEvent asyncEvent) throws IOException {
                                if (!wrappedResponse.isCommitted()) {
                                    //此刻缓存已完整
                                    wrappedResponse.copyBodyToResponse();
                                }
                            }

                            @Override
                            public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                                log.warn("Async request timeout");
                            }

                            @Override
                            public void onError(AsyncEvent asyncEvent) throws IOException {
                                log.warn("Async request error", asyncEvent.getThrowable());
                            }

                            @Override
                            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
                            }
                        });
                    } else {
                        wrappedResponse.copyBodyToResponse();
                    }
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
