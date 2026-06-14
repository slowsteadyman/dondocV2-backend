package com.dondoc.controller;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.FarmMembers;
import com.dondoc.dto.FarmMembers.FarmJoinResponse;
import com.dondoc.dto.Farms;
import com.dondoc.service.FarmService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farms")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService){
        this.farmService = farmService;
    }

    @GetMapping("")
    public ApiResponse<List<Farms.FarmGetResponse>> getFarmList(@RequestHeader("userId") Long userId) {
        return ApiResponse.ok(farmService.getFarmList(userId), "농장 목록 조회 성공");
    }

    @GetMapping("/members")
    public List<Farms.Member> getFarmMembers() {
        return farmService.getFarmMembers();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Farms.CreateResponse>> createFarm(
            @RequestHeader(value = "userId", required = false) Long userId,
            @RequestBody Farms.CreateRequest request
    ) {
        Farms.CreateResponse response = farmService.createFarm(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response, "농장 생성 성공"));
    }

    @PostMapping("/{farmId}")
    public ResponseEntity<ApiResponse<FarmJoinResponse>> addFarmMember(
            @RequestHeader("userId") long userId,
            @PathVariable long farmId) {
        FarmJoinResponse data = farmService.addFarmMember(userId, farmId);
        String message = "농장 가입 성공";
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.ok(data, message));
    }

    @PostMapping("/members")
    public void createFarmMember(@RequestBody Farms.Member farmMember){
        farmService.createFarmMember(farmMember);
    }
}
