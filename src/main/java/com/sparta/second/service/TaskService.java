package com.sparta.second.service;

import com.sparta.second.entity.Task;
import com.sparta.second.dto.TaskRequestDto;
import com.sparta.second.dto.TaskResponseDto;

public interface TaskService {

    // 일정 등록
    TaskResponseDto save(TaskRequestDto requestDto);

    // 일정 단건 조회
    TaskResponseDto get(Long taskId);

    // 일정 수정
    TaskResponseDto modify(Long taskId, TaskRequestDto requestDto);

    default Task dtoToEntity(TaskRequestDto dto) {
        Task task = Task.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .name(dto.getName())
                .deleteStatus(false)
                .build();
        return task;
    }

    default TaskResponseDto entityToDTO(Task task) {
        TaskResponseDto responseDto = TaskResponseDto.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .contents(task.getContents())
                .name(task.getName())
                .regDate(task.getRegDate())
                .modDate(task.getModDate())
                .build();
        return responseDto;
    }
}
