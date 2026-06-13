package com.dondoc.service;

import com.dondoc.dto.*;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDto.UserResponse> getUsers(){
        List<User> entities = userRepository.findAll();
        return entities.stream()
                .map(entity -> new UserDto.UserResponse(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getName(),
                        entity.getAge(),
                        entity.getCurrentPigLevel(),
                        entity.getCurrentHouseLevel(),
                        entity.getMonthlyIncome(),
                        entity.getTargetExpenseRatio(),
                        entity.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public void createUser(UserDto.CreateRequest dto){
        User user = new User(
                null,
                dto.getUserId(),
                dto.getUserPassword(),
                dto.getName(),
                dto.getAge(),
                dto.getCurrentPigLevel(),
                dto.getCurrentHouseLevel(),
                3,
                dto.getMonthlyIncome(),
                dto.getTargetExpenseRatio(),
                dto.getCreatedAt()
        );
        userRepository.save(user);
    }

    // monthlyBudget = 월수입 × 목표지출비율 / 100  -> 이번 달에 쓸 수 있는 총 예산
    // 예) 200만원 × 80% = 160만원

    // dailyBudget = 월예산 / 이번달 일수  -> 하루에 쓸 수 있는 예산
    // LocalDate.now().lengthOfMonth() → 이번달이 며칠인지 자동으로 계산해줌
    public UserDto.MeResponse getUserMe(Long userId){
        if (userId == null) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "인증되지 않은 사용자"
            );
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,"사용자를 찾을 수 없습니다."));

        long monthlyBudget = user.getMonthlyIncome() * user.getTargetExpenseRatio() / 100;
        long dailyBudget = monthlyBudget / LocalDate.now().lengthOfMonth();

        return new UserDto.MeResponse(
                user.getName(),
                user.getAge(),
                user.getCurrentPigLevel(),
                user.getCurrentHouseLevel(),
                user.getCurrentCharacterLevel(),
                user.getMonthlyIncome(),
                user.getTargetExpenseRatio(),
                monthlyBudget,
                dailyBudget
        );


    }

    public ApiResponse<UserDto.PatchResponse> updateUserMe(Long userId, UserDto.PatchRequest request){
        if (userId == null) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "인증되지 않은 사용자입니다."
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

        userRepository.update(userId, request, user);

        String updatedName = request.getName() != null ? request.getName() : user.getName();
        Integer updatedAge = request.getAge() != null ? request.getAge() : user.getAge();
        Long updatedMonthlyIncome = request.getMonthlyIncome() != null
                ? request.getMonthlyIncome()
                : user.getMonthlyIncome();
        Integer updatedTargetExpenseRatio = request.getTargetExpenseRatio() != null
                ? request.getTargetExpenseRatio()
                : user.getTargetExpenseRatio();

        long monthlyBudget = updatedMonthlyIncome * updatedTargetExpenseRatio / 100;
        long dailyBudget = monthlyBudget / LocalDate.now().lengthOfMonth();

        UserDto.PatchResponse data = new UserDto.PatchResponse(
                user.getId(),
                updatedName,
                updatedAge,
                updatedMonthlyIncome,
                updatedTargetExpenseRatio,
                monthlyBudget,
                dailyBudget
        );

        return new ApiResponse<>(true, data, "프로필 설정이 완료되었습니다.");

    }



}
