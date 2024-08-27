package com.sparta.second.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class TaskResponseDto {
    private Long taskId;
    private String title;
    private String contents;
    private String userName;
    private String weather;
    private int replyCount;
    private List<ManagerResponseDto> managerList;
    private LocalDate regDate;
    private LocalDate modDate;

}
