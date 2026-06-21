package com.dondoc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name="farm_members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FarmMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private Long userId;
    @Column(name="farm_id")
    private Long farmId;
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}
