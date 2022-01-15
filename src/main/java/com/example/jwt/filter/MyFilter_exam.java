package com.example.jwt.filter;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter_exam implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("테스트");
    }
}
