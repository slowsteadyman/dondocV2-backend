package com.dondoc.controller;

import com.dondoc.dto.auth.LoginRequest;
import com.dondoc.dto.auth.LoginResponse;
import com.dondoc.dto.auth.SignUpRequest;
import com.dondoc.dto.auth.SignUpResponse;
import com.dondoc.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        LoginResponse response = authService.loginUser(request);
        if(response == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                "success", false,
                "message", "아이디 또는 비밀번호가 일치하지 않습니다.",
                "data", new Object()
        ));
        else return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "로그인 성공",
                "data", response
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request){
        Long response = authService.createUser(request);

        if(response > 0L) {
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "회원가입이 완료되었습니다.",
                    "data", new SignUpResponse(response)
            ));
        } else return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "success", false,
                "message", "이미 사용 중인 아이디입니다.",
                "data", new Object()
        ));
    }
}
