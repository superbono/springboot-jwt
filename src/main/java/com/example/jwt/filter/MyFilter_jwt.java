package com.example.jwt.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter_jwt implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest rq = (HttpServletRequest) servletRequest;
        HttpServletResponse rs = (HttpServletResponse) servletResponse;
        // 토큰: 토큰을 만들어주고, id,pw의 요청이 제대로 요청되고 로그인이 성공하면, 토큰을 만들어주고 토큰을 전달해준다.
        // 요청할 때 마다 header안에 Authorization에 value값으로 토큰을 가져온다.
        // 토큰이 넘어오면 이 토큰이 본인이 만든것인지 검증만 하면 됨. (RSA, HS256)
        if(rq.getMethod().equals("POST")) {
            System.out.println("POST요청");
            String headerAuth = rq.getHeader("Authorization");
            System.out.println(headerAuth);

            if(headerAuth.equals("R")) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                PrintWriter out = servletResponse.getWriter();
                out.println("인증안됨");
            }
        }

        System.out.println("필터1번");

    }
}
