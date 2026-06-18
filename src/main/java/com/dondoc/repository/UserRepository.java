package com.dondoc.repository;

import com.dondoc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // save(), findAll(), findById()는 오버라이딩 메소드라 없어도 동작함
    Optional<User> findByUserId(String userId);
}