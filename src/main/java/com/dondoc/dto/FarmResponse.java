package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmResponse {
    private Long farmId;
    private String farmName;
    private Integer memberCount;
    private Boolean joined;
    private LocalDateTime createdAt;
}
