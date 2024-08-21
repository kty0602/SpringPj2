package com.sparta.second.service;

import com.sparta.second.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sparta.second.dto.TaskResponseDto;
import com.sparta.second.dto.TaskRequestDto;
import com.sparta.second.entity.Task;
import com.sparta.second.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    // 일정 등록
    @Override
    public TaskResponseDto save(TaskRequestDto requestDto) {
        Task task = dtoToEntity(requestDto);
        Task saveTask = taskRepository.save(task);
        return entityToDTO(saveTask);
    }

    // 일정 조회
    @Override
    public TaskResponseDto get(Long taskId) {
        Optional<Task> task = taskRepository.getTaskByTaskId(taskId);
        Task getTask = task.orElseThrow(() -> new NotFoundException("해당 일정이 존재하지 않거나 이미 삭제된 일정입니다."));
        return entityToDTO(getTask);
    }

    // 일정 수정
    @Transactional
    @Override
    public TaskResponseDto modify(Long taskId, TaskRequestDto requestDto) {
        Task task = taskRepository.getReferenceById(taskId);

        if(requestDto.getTitle() != null) {
            task.changeTitle(requestDto.getTitle());
        }
        if(requestDto.getContents() != null) {
            task.changeContent(requestDto.getContents());
        }
        if(requestDto.getName() != null) {
            task.changeName(requestDto.getName());
        }
        Task newTask = taskRepository.save(task);
        return entityToDTO(newTask);
    }
}
