package com.dondoc.service;

import com.dondoc.dto.auth.LoginRequest;
import com.dondoc.dto.auth.LoginResponse;
import com.dondoc.dto.auth.SignUpRequest;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Value("${app.default-farm-id}")
    private Long defaultFarmId;

    private final UserRepository userRepository;
    private final FarmService farmService;

    public AuthService(UserRepository userRepository, FarmService farmService){
        this.userRepository = userRepository;
        this.farmService = farmService;
    }

    @Transactional
    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId()).orElse(null);

        if(user == null || !user.getUserPassword().equals(request.getUserPassword())) {
            throw new ApiException(HttpStatus.CONFLICT, "아이디나 비밀번호가 일치하지 않습니다.");
        } else  {
            user.updateLastLoginTime();
            return new LoginResponse(
                    user.getId(),
                    user.getName(),
                    user.getAge(),
                    user.getMonthlyIncome(),
                    user.getTargetExpenseRatio(),
                    user.getCurrentPigLevel(),
                    user.getCurrentHouseLevel(),
                    user.getCurrentCharacterLevel()
            );
        }
    }

    @Transactional
    public Long createUser(SignUpRequest request){
        if(userRepository.findByUserId(request.getUserId()).isEmpty()) {
            User newUser = new User(
                    null,
                    request.getUserId(),
                    request.getUserPassword(),
                    request.getName(),
                    0,
                    5,
                    3,
                    3,
                    0L,
                    0,
                    LocalDateTime.now(),
                    null
            );

            User savedUser = userRepository.save(newUser);
            farmService.addFarmMember(savedUser.getId(), defaultFarmId);
            return savedUser.getId();
        } else {
            throw new ApiException(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다.");
        }
    }
}
