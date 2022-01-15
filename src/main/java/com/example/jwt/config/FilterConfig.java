package com.example.jwt.config;

import com.example.jwt.filter.MyFilter_jwt;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 커스텀필터를 사용할 수 있도록 설정하는 곳.
@Configuration
public class FilterConfig {

    @Bean
    FilterRegistrationBean<MyFilter_jwt> filter() {
        FilterRegistrationBean<MyFilter_jwt> bean = new FilterRegistrationBean<>(new MyFilter_jwt());
        bean.addUrlPatterns("/*");
        bean.setOrder(0); // 낮은 번호일수록 먼저 호출된다.
        return bean;
    }

}
