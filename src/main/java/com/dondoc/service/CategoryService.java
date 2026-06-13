package com.dondoc.service;

import com.dondoc.dto.CategoryDto;
import com.dondoc.entity.Category;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto.Response> getCategories(Long userId) {
        if (userId == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "인증 토큰 없음");
        }

        List<Category> entities = categoryRepository.findAll();
        return entities.stream()
                .map(entity -> new CategoryDto.Response(
                        entity.getId(),
                        entity.getName(),
                        entity.getType()
                ))
                .collect(Collectors.toList());
    }
}
