package com.sparta.second.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class UserResponseDto {
    private Long userId;
    private String name;
    private String email;
    private LocalDate regDate;
    private LocalDate modDate;
}
