package com.dondoc.service;

import com.dondoc.dto.farm.FarmDetailResponse;
import com.dondoc.entity.Farm;
import com.dondoc.entity.User;
import com.dondoc.exception.ApiException;
import com.dondoc.repository.FarmMemberRepository;
import com.dondoc.repository.FarmRepository;
import com.dondoc.repository.UserRepository;
import com.dondoc.repository.projection.FarmMemberDetail;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FarmDetailServiceTest {

    @Test
    void getFarmDetailReturnsFarmAndMembersWhenUserIsMember() {
        FarmDetailService service = new FarmDetailService(
                farmRepository(Optional.of(farm())),
                farmMemberRepository(true),
                userRepository(Optional.of(user(1L)))
        );

        FarmDetailResponse response = service.getFarmDetail("1", 1L);

        assertEquals(1L, response.getFarmId());
        assertEquals("기본 농장", response.getFarmName());
        assertEquals(2, response.getMemberCount());
        assertTrue(response.getJoined());
        assertEquals(1L, response.getMembers().get(0).getUserId());
        assertEquals("민호", response.getMembers().get(0).getName());
        assertEquals(5, response.getMembers().get(0).getCurrentPigLevel());
        assertEquals(3, response.getMembers().get(0).getCurrentHouseLevel());
        assertEquals(LocalDateTime.of(2026, 5, 26, 9, 10), response.getMembers().get(0).getJoinedAt());
    }

    @Test
    void getFarmDetailRejectsMissingUserIdHeader() {
        FarmDetailService service = new FarmDetailService(
                farmRepository(Optional.of(farm())),
                farmMemberRepository(true),
                userRepository(Optional.of(user(1L)))
        );

        ApiException exception = assertThrows(ApiException.class, () -> service.getFarmDetail(null, 1L));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("인증 토큰 없음", exception.getMessage());
    }

    @Test
    void getFarmDetailRejectsUnknownUser() {
        FarmDetailService service = new FarmDetailService(
                farmRepository(Optional.of(farm())),
                farmMemberRepository(true),
                userRepository(Optional.empty())
        );

        ApiException exception = assertThrows(ApiException.class, () -> service.getFarmDetail("1", 1L));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
        assertEquals("인증 토큰 없음", exception.getMessage());
    }

    @Test
    void getFarmDetailRejectsMissingFarm() {
        FarmDetailService service = new FarmDetailService(
                farmRepository(Optional.empty()),
                farmMemberRepository(true),
                userRepository(Optional.of(user(1L)))
        );

        ApiException exception = assertThrows(ApiException.class, () -> service.getFarmDetail("1", 999L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("존재하지 않는 농장", exception.getMessage());
    }

    @Test
    void getFarmDetailRejectsNonMember() {
        FarmDetailService service = new FarmDetailService(
                farmRepository(Optional.of(farm())),
                farmMemberRepository(false),
                userRepository(Optional.of(user(1L)))
        );

        ApiException exception = assertThrows(ApiException.class, () -> service.getFarmDetail("1", 1L));

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("농장 멤버가 아님", exception.getMessage());
    }

    private FarmRepository farmRepository(Optional<Farm> farm) {
        return new FarmRepository(null) {
            @Override
            public Optional<Farm> findById(Long id) {
                return farm;
            }
        };
    }

    private FarmMemberRepository farmMemberRepository(boolean member) {
        return new FarmMemberRepository(null) {
            @Override
            public boolean existsByFarmIdAndUserId(Long farmId, Long userId) {
                assertEquals(1L, farmId);
                assertEquals(1L, userId);
                return member;
            }

            @Override
            public List<FarmMemberDetail> findMemberDetailsByFarmId(Long farmId) {
                assertEquals(1L, farmId);
                return List.of(
                        new FarmMemberDetail(1L, "민호", 5, 3, LocalDateTime.of(2026, 5, 26, 9, 10)),
                        new FarmMemberDetail(2L, "아영", 4, 2, LocalDateTime.of(2026, 5, 26, 9, 30))
                );
            }
        };
    }

    private UserRepository userRepository(Optional<User> user) {
        return new UserRepository(null) {
            @Override
            public Optional<User> findById(Long id) {
                return user;
            }
        };
    }

    private Farm farm() {
        return new Farm(1L, "기본 농장", LocalDateTime.of(2026, 5, 26, 9, 0));
    }

    private User user(Long id) {
        return new User(
                id,
                "test-user",
                "password",
                "민호",
                20,
                5,
                3,
                3_000_000L,
                40,
                null
        );
    }
}
