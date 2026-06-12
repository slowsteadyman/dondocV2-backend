package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categories {
    private Long id;
    private String name;
    private String icon;
    private String type;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryDto {
        Long id;
        String name;
    }
}
