package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.Farms.FarmDetailResponse;
import com.dondoc.service.FarmDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/farms")
public class FarmDetailController {

    private final FarmDetailService farmDetailService;

    public FarmDetailController(FarmDetailService farmDetailService) {
        this.farmDetailService = farmDetailService;
    }

    @GetMapping("/{farmId}")
    public ApiResponse<FarmDetailResponse> getFarmDetail(
            @PathVariable Long farmId,
            @RequestHeader(value = "userId", required = false) String userId
    ) {
        return ApiResponse.ok(farmDetailService.getFarmDetail(userId, farmId), "농장 상세 조회 성공");
    }
}
