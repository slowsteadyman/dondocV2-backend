package com.dondoc.controller;

import com.dondoc.dto.FarmMembers;
import com.dondoc.dto.FarmResponse;
import com.dondoc.dto.Farms;
import com.dondoc.service.FarmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/farms")
public class FarmController {

    private final FarmService farmService;

    public FarmController(FarmService farmService){
        this.farmService = farmService;
    }

//    @GetMapping
//    public List<Farms> getFarms() {
//        return farmService.getFarms();
//    }

    @GetMapping("")
    public ResponseEntity<?> getFarmList(@RequestHeader("userId") Long userId) {
        List<FarmResponse> responseList = farmService.getFarmList(userId);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", responseList
        ));
    }

    @GetMapping("/members")
    public List<FarmMembers> getFarmMembers() {
        return farmService.getFarmMembers();
    }

    @PostMapping
    public void createFarm(@RequestBody Farms farm){
        farmService.createFarm(farm);
    }

    @PostMapping("/members")
    public void createFarmMember(@RequestBody FarmMembers farmMember){
        farmService.createFarmMember(farmMember);
    }


}
