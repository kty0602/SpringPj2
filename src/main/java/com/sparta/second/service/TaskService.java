package com.sparta.second.service;

import com.sparta.second.dto.*;
import com.sparta.second.entity.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    // 일정 등록
    TaskResponseDto save(TaskRequestDto requestDto);

    // 일정 단건 조회
    TaskResponseDto get(Long taskId);

    // 일정 전제 조회
    PageResultDto<TaskResponseDto, Object[]> getList(PageRequestDto pageRequestDto);

    // 일정 수정
    TaskResponseDto modify(Long taskId, TaskRequestDto requestDto);

    // 일정 삭제
    void delete(Long taskId);

    default Task dtoToEntity(TaskRequestDto dto) {
        Task task = Task.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .name(dto.getName())
                .deleteStatus(false)
                .build();
        return task;
    }

    default TaskResponseDto entityToDTO(Task task, Long replyCount) {
        TaskResponseDto responseDto = TaskResponseDto.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .contents(task.getContents())
                .name(task.getName())
                .regDate(task.getRegDate())
                .modDate(task.getModDate())
                .replyCount(replyCount.intValue())
                .build();
        return responseDto;
    }
}
