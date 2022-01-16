package com.example.jwt.config.jwt;

import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있다.
// login으로 요청이 왔을때 (username과 password를 post로 담아서) UsernamePasswordAuthenticationFilter가 동작한다.
// securityConfig에서 fomlogin().disable 처리했기 때문에 해당 요청을 보내도 작동하지 않는다.
// 그래서 JwtAuthenticationFilter를 securityConfig에 등록해줘야 한다.
public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {
    //private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;

    public JwtAuthorizationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // 로그인 시도를 할 수 있게 만들어 줘야 한다.
    // /login 요청을 하게 되면 해당 유저정보를 통해서 로그인 시도를 할 수 있게 된다.
    // authenticationManager로 로그인 시도를 하게 되면 PrincipalDetailsService가 호출
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthorizationFilter : 진입");
        // username, password를 입력후에
        // 로그인 시도해야함.
        return super.attemptAuthentication(request, response);
    }
}