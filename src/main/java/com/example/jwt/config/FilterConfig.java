package com.example.jwt.config;

import com.example.jwt.filter.MyFilter_jwt;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 커스텀필터를 사용할 수 있도록 설정하는 곳.
// 이렇게 설정하게 되면, SecurityConfig에서 설정한 필터가 있다면 그 필터가 먼저 실행되고, 여기서 설정한 필터들이 실행된다.
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
