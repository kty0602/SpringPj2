package com.sparta.second.service;

import com.sparta.second.dto.UserRequestDto;
import com.sparta.second.dto.UserResponseDto;
import com.sparta.second.entity.User;
import com.sparta.second.exception.AlreadyDeleteException;
import com.sparta.second.exception.NotFoundException;
import com.sparta.second.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // 유저 등록
    @Override
    public UserResponseDto save(UserRequestDto requestDto) {
        User user = dtoToEntity(requestDto);
        User saveUser = userRepository.save(user);
        return entityToDTO(saveUser);
    }

    // 유저 조회
    @Override
    public UserResponseDto get(Long userId) {
        Optional<User> user = userRepository.getUserByUserId(userId);
        User getUser = user.orElseThrow(() -> new NotFoundException("해당 유저가 존재하지 않습니다."));
        return entityToDTO(getUser);
    }

    @Override
    public List<UserResponseDto> getList() {
        List<User> users = userRepository.findAllActiveUsers();
        return users.stream().map(user ->
                entityToDTO(user)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserResponseDto modify(Long userId, UserRequestDto requestDto) {
        User user = userRepository.getReferenceById(userId);

        if(requestDto.getName() != null) {
            user.changeName(requestDto.getName());
        }
        if(requestDto.getEmail() != null) {
            user.changeEmail(requestDto.getEmail());
        }
        User newUser = userRepository.save(user);
        return entityToDTO(newUser);
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AlreadyDeleteException("해당 유저가 없습니다."));
        user.setDeleteStatus(true);
        // 삭제되는 유저와 연관된 일정들 삭제, 해당 일정의 댓글도 삭제, 해당 일정에 배치된 매니저들도 삭제
        user.getTaskList().forEach(task -> {
            task.setDeleteStatus(true);
            task.getReplyList().forEach(reply -> reply.setDeleteStatus(true));
            task.getManagerList().forEach(manager -> manager.setDeleteStatus(true));
        });
        user.getReplyList().forEach(reply -> reply.setDeleteStatus(true));
        user.getManagerList().forEach(manager -> manager.setDeleteStatus(true));
    }
}
