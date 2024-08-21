package com.sparta.second.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class ReplyResponseDto {
    private Long replyId;
    private Long taskId;
    private String contents;
    private String name;
    private LocalDate regDate;
    private LocalDate modDate;
}
