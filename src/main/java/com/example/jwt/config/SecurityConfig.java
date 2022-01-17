package com.example.jwt.config;


import com.example.jwt.config.jwt.JwtAuthenticationFilter;
import com.example.jwt.config.jwt.JwtAuthorizationFilter;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final CorsFilter corsFilter;

    // 시큐리티 암호화 Bean으로 등록
    // Bean으로 등록하게 되면 해당 메서드의 리턴되는 오브젝트를 Ioc로 등록해준다.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 이것만 추가해줄 시에는 오류를 발생시킨다. Security안에 있는 필터가 아닌 일반적인 Filter를 사용했기 때문이다.
        // http.addFilter(new MyFilter_jwt());
        // 그래서 아래와 같이 before, after를 통해서 걸어야 된다.
        // http.addFilterBefore(new MyFilter_jwt(), BasicAuthenticationFilter.class);
        // 이런 커스텀필터를 securityConfig에서 정의하지 않아도 된다.
        // 필터만 정의할 수 있는 filterConfig를 만들어서 이곳에 정의해도 된다.

        //http.addFilterBefore(new MyFilter_jwt(), SecurityContextPersistenceFilter.class);

        // csrf는 Cross site Request forgery를 말하며, 정상적인 사용자가 의도치 않은 위조요청을 보내는 것을 말한다.
        http.csrf().disable();
        // Session을 사용하지 않겠다는 의미
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 필터를 추가해 주겠다고 설정하는곳
            .addFilter(corsFilter) // 인증이 필요하지 않은 곳에는 @CrossOrigin으로 해결가능/ 인증이 필요한 곳은 필터를 등록해줘야 한다.
            .addFilter(new JwtAuthenticationFilter(authenticationManager()) )// formLogin().disable()했기 때문에, /login의 요청을 받기 위해 JwtAuthenticationFilter를 등록해줬는데,
            // addFilter에서 받아야 하는 파라미터가 있다. AuthenticationManager를 보내줘야한다.
            // WebSecurityConfigurerAdapter안에 AuthenticationManager가 있다.
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository) )// formLogin().disable()했기 때문에, /login의 요청을 받기 위해 JwtAuthenticationFilter를 등록해줬는데,
            // addFilter에서 받아야 하는 파라미터가 있다. AuthenticationManager를 보내줘야한다.
            // WebSecurityConfigurerAdapter안에 AuthenticationManager가 있다.
            .formLogin().disable()
            .httpBasic().disable() // http로 요청 보낼때에는 노출이 될 가능성이 있다. / https(Secure)로 보냈을 시에는 노출x
                                   // 기존 httpBasic을 사용할 경우에, 폼로그인을 통해서 header id, pw를 보내게 되는데 이때에는 개인정보가 노출될 수 있다.
                                   // 그래서 header안에 Authorization속성을 추가해서 token을 생성해서 전달하는 방법을 쓴다.
                                   // 이런 방식을 Bearer(Token을 들고가는 방식)라고 한다.
                                   // token을 사용하게 될 경우, 유효시간이 되면 토큰은 사라진다.
            .authorizeRequests()
            .antMatchers("/api/v1/user/**")
            .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/manager/**")
            .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
            .antMatchers("/api/v1/admin/**")
            .access("hasRole('ROLE_ADMIN')")
            .anyRequest().permitAll();
    }
}
