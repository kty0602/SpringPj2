package com.sparta.second.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TaskListResponseDto {
    private Long taskId;
    private String title;
    private String contents;
    private String userName;
    private int replyCount;
    private LocalDate regDate;
    private LocalDate modDate;
}
