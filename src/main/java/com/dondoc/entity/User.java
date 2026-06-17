package com.dondoc.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userPassword;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private Integer currentPigLevel;
    @Column(nullable = false)
    private Integer currentHouseLevel;
    @Column(nullable = false)
    private Integer currentCharacterLevel;

    @Setter
    @Column(nullable = false)
    private Long monthlyIncome;

    @Setter
    @Column(nullable = false)
    private Integer targetExpenseRatio;

    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;

    public void updateLastLoginTime() {
        this.lastLoginAt = LocalDateTime.now();
    }
}
