package com.dondoc.controller;

import com.dondoc.service.AuthService;
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
    public ResponseEntity<?> login(){
        return ResponseEntity.ok(Map.of());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(){
        return ResponseEntity.ok(Map.of());
    }
}
