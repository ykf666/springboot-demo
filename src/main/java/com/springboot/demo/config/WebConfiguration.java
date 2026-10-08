package com.springboot.demo.config;

import com.springboot.demo.filter.CacheBodyFilter;
import com.springboot.demo.filter.CacheBodyTeeFilter;
import com.springboot.demo.interceptor.TransactionMonitorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by yankefei on 2021/9/5.
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private TransactionMonitorInterceptor transactionMonitorInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决 SWAGGER 404报错
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 注册业务响应码校验拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(transactionMonitorInterceptor)
                .addPathPatterns("/**")
                .order(0);
    }

    /**
     * 注册请求体缓存过滤器
     */
    @Bean
    public FilterRegistrationBean<CacheBodyTeeFilter> cacheBodyFilterRegistration() {
        FilterRegistrationBean<CacheBodyTeeFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CacheBodyTeeFilter());
        registration.addUrlPatterns("/*");
        registration.setName("cacheBodyFilter");
        registration.setOrder(1);
        return registration;
    }
}
