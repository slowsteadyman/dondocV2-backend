package com.dondoc.service;

import com.dondoc.dto.Users;
import com.dondoc.entity.User;
import com.dondoc.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void loginUser(){}

    public void createUser(Users dto){
        User user = new User(
                null,
                dto.getUserId(),
                dto.getUserPassword(),
                dto.getName(),
                dto.getAge(),
                dto.getCurrentPigLevel(),
                dto.getCurrentHouseLevel(),
                dto.getMonthlyIncome(),
                dto.getTargetExpenseRatio(),
                dto.getCreatedAt()
        );
        userRepository.save(user);
    }
}
