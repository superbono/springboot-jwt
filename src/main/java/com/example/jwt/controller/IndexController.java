package com.example.jwt.controller;


import com.example.jwt.model.User;
import com.example.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

// CrossOrigin을 설정할때 corsFilter를 사용하지 않고 @CrossOrigin을 통해서 설정할 수 있지만,
// 이때에는 security에서 인증이 필요한곳의 요청은 거부된다.

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api/v1/")
public class IndexController {

    private final UserRepository repository;
    //private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("index")
    public String index() {
        return "<h1>Index</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>Token12</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        repository.save(user);
        return "회원가입완료";
    }

    @GetMapping("/api/v1/user")
    public String user() {
        return "user";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }

}
