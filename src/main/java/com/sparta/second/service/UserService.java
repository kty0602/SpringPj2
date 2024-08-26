package com.sparta.second.service;

import com.sparta.second.dto.LoginRequestDto;
import com.sparta.second.dto.UserRequestDto;
import com.sparta.second.dto.UserResponseDto;
import com.sparta.second.entity.User;
import com.sparta.second.entity.UserRole;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {

    // 유저 등록
    UserResponseDto save(UserRequestDto requestDto);

    // jwt용 유저 등록
    String saveJwt(UserRequestDto requestDto);

    // 유저 로그인
    String login(LoginRequestDto requestDto, HttpServletResponse res);

    // 유저 단건 조회
    UserResponseDto get(Long userId);

    // 유저 전체 조회
    List<UserResponseDto> getList();

    // 유저 수정
    UserResponseDto modify(Long userId, UserRequestDto requestDto);

    // 유저 삭제
    void delete(Long userId);

    default User dtoToEntity(UserRequestDto dto) {
        UserRole role = null;
        if (dto.getRole().equals("USER")) {
            role = UserRole.USER;
        } else if (dto.getRole().equals("ADMIN")) {
            role = UserRole.ADMIN;
        } else {
            throw new IllegalArgumentException("잘못된 role: " + dto.getRole());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(role)
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
