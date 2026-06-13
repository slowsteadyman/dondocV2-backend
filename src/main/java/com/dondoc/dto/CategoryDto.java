package com.dondoc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CategoryDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private Long id;
        private String name;
        private String icon;
        private String type;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String type;
    }
}
