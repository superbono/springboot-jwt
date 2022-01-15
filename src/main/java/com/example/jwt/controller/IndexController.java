package com.example.jwt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// CrossOrigin을 설정할때 corsFilter를 사용하지 않고 @CrossOrigin을 통해서 설정할 수 있지만,
// 이때에는 security에서 인증이 필요한곳의 요청은 거부된다.

@RestController
public class IndexController {

    @GetMapping("/index")
    public String index() {
        return "<h1>Index</h1>";
    }

    @PostMapping("/token")
    public String token() {
        return "<h1>Token</h1>";
    }

}
