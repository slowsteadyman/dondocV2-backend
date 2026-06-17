package com.dondoc.repository;


import com.dondoc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAll();

    // 1인 유저 한 명만 조회
    Optional<User> findById(Long id);

    Optional<User> findByUserId(String userId);

    User save(User user);
}