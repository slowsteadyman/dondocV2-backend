package com.dondoc.repository;

import com.dondoc.entity.FarmMember;
import com.dondoc.repository.projection.FarmMemberCount;
import com.dondoc.repository.projection.FarmMemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public interface FarmMemberRepository extends JpaRepository<FarmMember,Long> {

    boolean existsByFarmIdAndUserId(Long farmId, Long userId);
    Optional<FarmMember> findByUserIdAndFarmId(long userId, long farmId);
    @Query(value = """
                SELECT
                    u.id AS userId,
                    u.name AS name,
                    u.current_pig_level AS currentPigLevel,
                    u.current_house_level AS currentHouseLevel,
                    fm.joined_at AS joinedAt
                FROM farm_members fm
                INNER JOIN users u ON fm.user_id = u.id
                WHERE fm.farm_id = :farmId
                ORDER BY fm.joined_at ASC, u.id ASC
""",nativeQuery = true)
    List<FarmMemberDetail> findMemberDetailsByFarmId(@Param("farmId") Long farmId);


    @Query("select fm.farmId from FarmMember fm where fm.userId = :userId")
     List<Long> findJoinedFarmIdsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from FarmMember fm where fm.farmId=:farmId and fm.userId=:userId")
    int deleteByFarmIdAndUserId(@Param("farmId") Long farmId,@Param("userId") Long userId);

    int countByFarmId(Long farmId);

    @Query("""
        select fm.farmId as farmId, count (fm.id) as memberCount
        from FarmMember fm
        group by fm.farmId
""")
    List<FarmMemberCount> findMemberCountRows();

    default Map<Long, Integer> findMemberCount() {
        return findMemberCountRows().stream()
                .collect(Collectors.toMap(FarmMemberCount::getFarmId,
                        row->Math.toIntExact(row.getMemberCount())
                ));
    }





}
