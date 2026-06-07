package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FarmLeaveResponse {
    private Long farmId;
    private Long userId;
}
