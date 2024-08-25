package com.sparta.second.service;

import com.sparta.second.dto.ManagerRequestDto;
import com.sparta.second.dto.ManagerResponseDto;
import com.sparta.second.entity.Manager;
import com.sparta.second.entity.Task;
import com.sparta.second.entity.User;

import java.util.List;

public interface ManagerService {
    List<ManagerResponseDto> save(List<ManagerRequestDto> managerRequestDtoList);

    default Manager dtoToEntity(ManagerRequestDto requestDto) {
        User user = User.builder().userId(requestDto.getUserId()).build();
        Task task = Task.builder().taskId(requestDto.getTaskId()).build();

        Manager manager = Manager.builder()
                .user(user)
                .task(task)
                .deleteStatus(false)
                .build();
        return manager;
    }

    default ManagerResponseDto entityToDTO(Manager manager, User user, Task task) {
        ManagerResponseDto dto = ManagerResponseDto.builder()
                .managerId(manager.getManagerId())
                .userName(user.getName())
                .email(user.getEmail())
                .build();
        return dto;
    }
}
