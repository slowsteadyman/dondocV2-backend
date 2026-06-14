package com.dondoc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recorde {
    private Long id;
    private Long userId;
    private Long categoryId;
    private Long amount;
    private String description;
    private String memo;
    private LocalDate recordDate;
    private LocalDateTime createdAt;
}
