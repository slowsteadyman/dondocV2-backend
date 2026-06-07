package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    // RecordSaveResponse에 사용됨
    // 이게 맞나 싶음
    Long id;
    String name;
}
