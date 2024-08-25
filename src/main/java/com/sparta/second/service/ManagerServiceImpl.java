package com.sparta.second.service;

import com.sparta.second.dto.ManagerRequestDto;
import com.sparta.second.dto.ManagerResponseDto;
import com.sparta.second.entity.Manager;
import com.sparta.second.entity.Task;
import com.sparta.second.entity.User;
import com.sparta.second.exception.NotFoundException;
import com.sparta.second.repository.ManagerRepository;
import com.sparta.second.repository.TaskRepository;
import com.sparta.second.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ManagerRepository managerRepository;
    @Override
    public List<ManagerResponseDto> save(List<ManagerRequestDto> managerRequestDtoList) {
        List<Manager> managers = new ArrayList<>();
        for(ManagerRequestDto requestDto : managerRequestDtoList) {
            Manager manager = dtoToEntity(requestDto);

            // 올바른 유저, 일정 인지 확인
            userRepository.getUserByUserId(requestDto.getUserId())
                    .orElseThrow(() -> new NotFoundException("해당 유저는 존재하지 않습니다!"));
            taskRepository.getTaskByTaskId(requestDto.getTaskId())
                    .orElseThrow(() -> new NotFoundException("해당 일정은 존재하지 않습니다!"));

            Manager saveManager = managerRepository.save(manager);

            managers.add(saveManager);
        }
        return managers.stream().map(manager ->
                entityToDTO(manager, manager.getUser(), manager.getTask())).collect(Collectors.toList());
    }
}
