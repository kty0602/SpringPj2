package com.sparta.second.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor // TaskService에서 생성자에 접근할 수 있도록 추가
public class ManagerResponseDto {
    private Long managerId;
    private String userName;
    private String email;
}
