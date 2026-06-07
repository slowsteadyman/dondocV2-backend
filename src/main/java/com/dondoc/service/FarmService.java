package com.dondoc.service;

import com.dondoc.dto.FarmMembers;
import com.dondoc.dto.FarmResponse;
import com.dondoc.dto.Farms;
import com.dondoc.entity.Farm;
import com.dondoc.entity.FarmMember;
import com.dondoc.repository.FarmMemberRepository;
import com.dondoc.repository.FarmRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public List<FarmResponse> getFarmList(Long userId) {
        List<Farm> farms = farmRepository.findAll();
        Map<Long, Integer> memberCountMap = farmMemberRepository.findMemberCount();
        List<Long> joinedFarms = farmMemberRepository.findJoinedFarmIdsByUserId(userId);


        return farms.stream().map(farm -> {
            FarmResponse farmResponse = new FarmResponse();

            farmResponse.setFarmId(farm.getId());
            farmResponse.setFarmName(farm.getName());
            farmResponse.setCreatedAt(farm.getCreatedAt());

            farmResponse.setMemberCount(memberCountMap.getOrDefault(farm.getId(), 0));

            farmResponse.setJoined(joinedFarms.contains(farm.getId()));

            return farmResponse;
        }).collect(Collectors.toList());
    }
}
