package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.UserDto;
import com.dondoc.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class    UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService =userService;
    }

    @GetMapping
    public List<UserDto.UserResponse> getUsers(){
        return userService.getUsers();
    }

    // PostMapping - POST 요청을 받는 엔드포인트
    // @RequestBody - 요청 body의 JSON을 UserDto.CreateRequest 객체로 변환
    @PostMapping
    public void createUser(@RequestBody UserDto.CreateRequest user){
        userService.createUser(user);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserMe(
            @RequestHeader(value = "userId", required = false) Long userId) {
        UserDto.MeResponse response = userService.getUserMe(userId);
        return ResponseEntity.ok(ApiResponse.ok(response, "월별 요약 통계 조회 성공"));
    }

    @PatchMapping("/me")
    public ResponseEntity<?> updateUserMe(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestBody UserDto.PatchRequest request){
        return ResponseEntity.ok(userService.updateUserMe(userId, request));
    }
}
