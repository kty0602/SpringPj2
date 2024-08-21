package com.sparta.second.dto;

import lombok.Data;

@Data
public class ReplyRequestDto {
    private String contents;
    private Long taskId;
    private String name;
}
