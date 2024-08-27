package com.sparta.second.service;

import com.sparta.second.dto.*;
import com.sparta.second.entity.Task;
import com.sparta.second.entity.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public interface TaskService {

    // 일정 등록
    TaskResponseDto save(TaskRequestDto requestDto);

    // 일정 단건 조회
    TaskResponseDto get(Long taskId);

    // 일정 전제 조회
    PageResultDto<TaskListResponseDto, Object[]> getList(PageRequestDto pageRequestDto);

    // 일정 수정
    TaskResponseDto modify(Long taskId, TaskRequestDto requestDto);

    // 일정 삭제
    void delete(Long taskId);

    default Task dtoToEntity(TaskRequestDto dto) {
        User user = User.builder().userId(dto.getUserId()).build();

        Task task = Task.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .user(user)
                .deleteStatus(false)
                .build();
        return task;
    }

    // 6단계 일정 단건 조회 시 담당 유저 목록 출력을 위함
    default TaskResponseDto entityToDTO(Task task, User user, Long replyCount) {
        TaskResponseDto responseDto = TaskResponseDto.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .contents(task.getContents())
                .userName(user.getName())
                .weather(task.getWeather())
                .regDate(task.getRegDate())
                .modDate(task.getModDate())
                // 일정 등록 후 반환을 하기에, 초반 담당 유저가 배치되지 않아서 null를 반환하는 문제가 있어 null이면 초기화 하도록 변경
                .managerList(task.getManagerList() != null ?
                        task.getManagerList().stream()
                                .filter(manager -> !manager.isDeleteStatus()) // deleteStatus가 false인 담당유저만 나오도록 필터링
                                .map(manager -> new ManagerResponseDto(
                                        manager.getManagerId(),
                                        manager.getUser().getName(),
                                        manager.getUser().getEmail()
                                )).collect(Collectors.toList()) :
                        new ArrayList<>())
                .replyCount(replyCount.intValue())
                .build();
        return responseDto;
    }

    // 6단계 전체 일정 조회 시 담당 유저가 포함되지 않기 위함
    default TaskListResponseDto entityToDTO1(Task task, User user, Long replyCount) {
        TaskListResponseDto responseDto = TaskListResponseDto.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .contents(task.getContents())
                .userName(user.getName())
                .regDate(task.getRegDate())
                .modDate(task.getModDate())
                .replyCount(replyCount.intValue())
                .build();
        return responseDto;
    }
}
