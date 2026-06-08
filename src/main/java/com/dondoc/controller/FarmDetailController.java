package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.farm.FarmDetailResponse;
import com.dondoc.service.FarmDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/farms")
@Tag(name = "Farm Detail", description = "농장 상세 조회 API")
public class FarmDetailController {

    private final FarmDetailService farmDetailService;

    public FarmDetailController(FarmDetailService farmDetailService) {
        this.farmDetailService = farmDetailService;
    }

    @GetMapping("/{farmId}")
    @Operation(summary = "농장 상세 조회", description = "농장 멤버만 농장 정보와 멤버 목록을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 토큰 없음", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "농장 멤버가 아님", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 농장", content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ApiResponse<FarmDetailResponse> getFarmDetail(
            @Parameter(name = "farmId", description = "농장 ID", required = true, example = "1", in = ParameterIn.PATH)
            @PathVariable Long farmId,
            @Parameter(name = "userId", description = "사용자 ID", required = true, example = "1", in = ParameterIn.HEADER)
            @RequestHeader(value = "userId", required = false) String userId
    ) {
        return ApiResponse.success(farmDetailService.getFarmDetail(userId, farmId));
    }
}
