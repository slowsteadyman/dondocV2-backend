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

    public List<Users.UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(user -> new Users.UserResponse(
                        user.getId(),
                        user.getUserId(),
                        user.getName(),
                        user.getAge(),
                        user.getCurrentPigLevel(),
                        user.getCurrentHouseLevel(),
                        user.getMonthlyIncome(),
                        user.getTargetExpenseRatio(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public Users.MeResponse getUserMe(Long userId){
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

        return new Users.MeResponse(
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

    public ApiResponse<Users.PatchResponse> updateUserMe(Long userId, Users.PatchRequest request){
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

        Users.PatchResponse data = new Users.PatchResponse(
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
