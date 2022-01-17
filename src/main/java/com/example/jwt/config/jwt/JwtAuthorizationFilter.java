package com.example.jwt.config.jwt;

import com.example.jwt.config.auth.PrincipalDetails;
import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있다.
// login으로 요청이 왔을때 (username과 password를 post로 담아서) UsernamePasswordAuthenticationFilter가 동작한다.
// securityConfig에서 fomlogin().disable 처리했기 때문에 해당 요청을 보내도 작동하지 않는다.
// 그래서 JwtAuthenticationFilter를 securityConfig에 등록해줘야 한다.
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends UsernamePasswordAuthenticationFilter {


    private final AuthenticationManager authenticationManager;
//    private UserRepository userRepository;

//    public JwtAuthorizationFilter(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

//    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
//        super(authenticationManager);
//        this.userRepository = userRepository;
//    }

    // 로그인 시도를 할 수 있게 만들어 줘야 한다.
    // /login 요청을 하게 되면 해당 유저정보를 통해서 로그인 시도를 할 수 있게 된다.
    // authenticationManager로 로그인 시도를 하게 되면 PrincipalDetailsService가 호출
    // loadUserByUsername가 실행 (PrincipalDetailsService안에 구현된 함수)
    // 그다음 PrincipalDetails를 세션에 담고 -> PrincipalDetails을 세션에 안담으면 권한관리가 안된다. => USER, MANAGER, ADMIN
    // jwt토큰을 만들어 응답
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthorizationFilter : 진입");
        // username, password를 입력후에
        // 로그인 시도해야함.


        try {
            ObjectMapper om = new ObjectMapper();
            User user = om.readValue(request.getInputStream(), User.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("유저정보"+principalDetails.getUser().getUsername());
            System.out.println("=====================1");
            // authentication 객체가 session영역에 저장됨.
            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }
            System.out.println("=====================2");
            return null;

    }
}