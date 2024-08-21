package com.sparta.second.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class TaskResponseDto {
    private Long taskId;
    private String title;
    private String contents;
    private String name;
    private LocalDate regDate;
    private LocalDate modDate;

}
