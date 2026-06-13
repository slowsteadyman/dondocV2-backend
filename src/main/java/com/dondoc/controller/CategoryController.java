package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.CategoryDto;
import com.dondoc.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto.Response>>> getCategories(
            @RequestHeader(value = "userId", required = false) Long userId
    ) {
        List<CategoryDto.Response> categories = categoryService.getCategories(userId);
        return ResponseEntity.ok(ApiResponse.ok(categories, "카테고리 조회 성공"));
    }
}
