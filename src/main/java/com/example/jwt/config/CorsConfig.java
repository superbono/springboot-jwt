package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// CrossOrigin을 설정하는 곳
// 여기서는 CrossOrigin에 관한 내용을 설정하고, SecurityConfig에 가서 기존의 내용을 사용하겠다고 선언해줘야 한다.

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답할 때 json을 자바스크립트에서 처리할 수 있도록 설정 ex) axios, ajax, fetch로 요청허용
        config.addAllowedOrigin("*"); // 모든 ip요청을 허용
        config.addAllowedHeader("*"); // 모든 header의 요청 허용
        config.addAllowedMethod("*"); // 모든 http method의 요청 허용(GET, POST, PUT, DELETE)
        source.registerCorsConfiguration("/api/",config);
        return new CorsFilter(source);
    }



}
