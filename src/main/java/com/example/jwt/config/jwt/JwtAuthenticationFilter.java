package com.example.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;


// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있다.
// login으로 요청이 왔을때 (username과 password를 post로 담아서) UsernamePasswordAuthenticationFilter가 동작한다.
// securityConfig에서 fomlogin().disable 처리했기 때문에 해당 요청을 보내도 작동하지 않는다.
// 그래서 JwtAuthenticationFilter를 securityConfig에 등록해줘야 한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


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

            // DB에 있는 username과 password가 일치한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println("로그인완료 -> username:  "+principalDetails.getUser().getUsername());
            System.out.println("=====================1");
            // authentication 객체가 session영역에 저장됨.
            // authentication 객체를 리턴하는 이유는 권한 관리를 security가 대신해주기 때문에 authentication을 리턴해준다.
            // JWT 토큰을 사용하면 따로 security session영역에 저장하지 않아도 된다. 그래도 리턴을 하는 이유는 권한 처리를 하기 위함이다.

            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

            System.out.println("=====================2");
            return null;
    }

    // attemptAuthentication에서 인증이 완료되고 종료되면 successfulAuthentication 함수가 실행된다.
    // successfulAuthentication함수 안에서 jwt 토큰을 만든다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication: 인증완료");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA방식x , Hash암호방식
        String jwtToken = JWT.create()
                             .withSubject("R_token")
                             .withExpiresAt(new Date(System.currentTimeMillis()+ JwtProperties.EXPIRATION_TIME))
                             .withClaim("id",principalDetails.getUser().getId())
                             .withClaim("username",principalDetails.getUser().getUsername())
                             .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        /*
        * 1.일반적인 방법
        * 요청할때마다 클라이언트 쿠키에 담은 세션ID를 항상 들고 서버에 요청할 시에 사용됨.
        * session.getAttribute("세션값 확인")
        * 서버는 클라이언트 요청이 오면, 세션ID가 유효한지 판단후에 세션ID가 유효할 경우, 인증이 필요한 페이지로 접근한다.
        *
        * 2.formLogin방식을 사용하지 않는 경우
        * username, password의 인증이 완료된 경우,
        * JWT 토큰을 생성
        * 클라이언트쪽으로 jwt토큰을 응답해준다 (header의 Authorization속성 안에 넣어준다.)
        * 요청시에는 세션ID가 아닌, JWT토큰을 가지고 요청해야 한다.
        * 서버는 JWT토큰이 유효한지를 판단 (필터를 만들어줘야 한다.)
        */
        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX+jwtToken);
    }
}