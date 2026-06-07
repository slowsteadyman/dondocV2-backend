package com.dondoc.service;

import com.dondoc.dto.ApiResponse;
import com.dondoc.dto.FarmLeaveResponse;
import com.dondoc.dto.FarmMembers;
import com.dondoc.dto.Farms;
import com.dondoc.entity.Farm;
import com.dondoc.entity.FarmMember;
import com.dondoc.repository.FarmMemberRepository;
import com.dondoc.repository.FarmRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmService {
    private final FarmRepository farmRepository;
    private final FarmMemberRepository farmMemberRepository;

    public FarmService(FarmRepository farmRepository, FarmMemberRepository farmMemberRepository){
        this.farmRepository = farmRepository;
        this.farmMemberRepository = farmMemberRepository;
    }

    public List<Farms> getFarms(){
        List<Farm> entities = farmRepository.findAll();
        return entities.stream()
                .map(entity -> new Farms(
                        entity.getId(),
                        entity.getName(),
                        entity.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<FarmMembers> getFarmMembers(){
        List<FarmMember> entities = farmMemberRepository.findAll();
        return entities.stream()
                .map(entity -> new FarmMembers(
                        entity.getId(),
                        entity.getUserId(),
                        entity.getFarmId(),
                        entity.getJoinedAt()
                )).collect(Collectors.toList());
    }

    public void createFarm(Farms dto){
        Farm farm = new Farm(
                null, dto.getName(), dto.getCreatedAt()
        );
        farmRepository.save(farm);
    }

    public void createFarmMember(FarmMembers dto){
        FarmMember farmMember = new FarmMember(
                null, dto.getUserId(), dto.getFarmId(), dto.getJoinedAt()
        );
        farmMemberRepository.save(farmMember);

    }

    //  1. farm_members에서 해당 유저 삭제
    //  2. 남은 멤버 수 확인
    //  3. 0명이면 농장도 삭제
    //  4. 응답 반환 (명세서에 message 없어서 null)
    public ApiResponse<FarmLeaveResponse> leaveFarm(Long farmId, Long userId) {
        farmMemberRepository.deleteByFarmIdAndUserId(farmId, userId);

        int remainCount = farmMemberRepository.countByFarmId(farmId);
        if (remainCount == 0) {
            farmRepository.deleteById(farmId);
        }

        FarmLeaveResponse data = new FarmLeaveResponse(farmId, userId);
        return new ApiResponse<>(true, data, null);
    }

}
