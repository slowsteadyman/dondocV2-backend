package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordSaveRequest {
    private String type;
    private Long categoryId;
    private LocalDate date;
    private Long amount;
    private String description;
    private String memo;
}
