package com.dondoc.controller;

import com.dondoc.dto.FarmDto;
import com.dondoc.service.FarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farm")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService){
        this.farmService = farmService;
    }

    @GetMapping
    public List<FarmDto.Farm> getFarms() {
        return farmService.getFarms();
    }

    @GetMapping("/members")
    public List<FarmDto.Member> getFarmMembers() {
        return farmService.getFarmMembers();
    }

    @PostMapping
    public void createFarm(@RequestBody FarmDto.Farm farm){
        farmService.createFarm(farm);
    }

    @PostMapping("/members")
    public void createFarmMember(@RequestBody FarmDto.Member farmMember){
        farmService.createFarmMember(farmMember);
    }

    @DeleteMapping("/{farmId}")
    public ResponseEntity<?> leaveFarm(
            @RequestHeader(value = "userId", required = false) Long userId,
            @PathVariable Long farmId) {
        return ResponseEntity.ok(farmService.leaveFarm(farmId, userId));
    }


}
