package com.sparta.second.service;

import com.sparta.second.dto.UserRequestDto;
import com.sparta.second.dto.UserResponseDto;
import com.sparta.second.entity.User;

import java.util.List;

public interface UserService {

    // 유저 등록
    UserResponseDto save(UserRequestDto requestDto);

    // 유저 단건 조회
    UserResponseDto get(Long userId);

    // 유저 전체 조회
    List<UserResponseDto> getList();

    // 유저 수정
    UserResponseDto modify(Long userId, UserRequestDto requestDto);

    // 유저 삭제
    void delete(Long userId);

    default User dtoToEntity(UserRequestDto dto) {
        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .deleteStatus(false)
                .build();
        return user;
    }

    default UserResponseDto entityToDTO(User user) {
        UserResponseDto dto = UserResponseDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .regDate(user.getRegDate())
                .modDate(user.getModDate())
                .build();
        return dto;
    }
}
