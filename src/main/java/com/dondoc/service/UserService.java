package com.dondoc.service;

import com.dondoc.dto.Users;
import com.dondoc.entity.User;
import com.dondoc.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<Users> getUsers(){
        List<User> entities = userRepository.findAll();
        return entities.stream()
                .map(entity -> new Users(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getUserPassword(),
                        entity.getName(),
                        entity.getAge(),
                        entity.getCurrentPigLevel(),
                        entity.getCurrentHouseLevel(),
                        entity.getCurrentCharacterLevel(),
                        entity.getMonthlyIncome(),
                        entity.getTargetExpenseRatio(),
                        entity.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void createUser(Users dto){
        User user = new User(
                null,
                dto.getUserId(),
                dto.getUserPassword(),
                dto.getName(),
                dto.getAge(),
                dto.getCurrentPigLevel(),
                dto.getCurrentHouseLevel(),
                dto.getCurrentCharacterLevel(),
                dto.getMonthlyIncome(),
                dto.getTargetExpenseRatio(),
                dto.getCreatedAt()
        );
        userRepository.save(user);
    }


}
