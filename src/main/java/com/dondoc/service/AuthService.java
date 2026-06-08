package com.dondoc.service;

import com.dondoc.dto.auth.LoginRequest;
import com.dondoc.dto.auth.LoginResponse;
import com.dondoc.dto.auth.SignUpRequest;
import com.dondoc.entity.User;
import com.dondoc.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByUserId(request.getUserId()).orElse(null);

        if(user == null || !user.getUserPassword().equals(request.getUserPassword())) {
            return null;
        } else  {
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

    public Long createUser(SignUpRequest request){
        if(userRepository.findByUserId(request.getUserId()).isEmpty()) {
            return userRepository.save(request);
        } else {
            return -1L;
        }
    }
}
