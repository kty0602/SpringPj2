package com.sparta.second.dto;

import lombok.*;
import org.springframework.data.domain.*;

@Builder
@Data
public class PageRequestDto {

    private int page;
    private int size;

    public PageRequestDto(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page, size, sort);
    }
}
