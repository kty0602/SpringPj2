package com.sparta.second.service;

import com.sparta.second.dto.LoginRequestDto;
import com.sparta.second.dto.UserRequestDto;
import com.sparta.second.dto.UserResponseDto;
import com.sparta.second.entity.User;
import com.sparta.second.entity.UserRole;
import com.sparta.second.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sparta.second.exception.AlreadyDeleteException;
import com.sparta.second.exception.NotFoundException;
import com.sparta.second.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 유저 등록
    @Override
    public UserResponseDto save(UserRequestDto requestDto) {
        String password = passwordEncoder.encode(requestDto.getPassword());
        User user = dtoToEntity(requestDto);
        user.setPassword(password);
        User saveUser = userRepository.save(user);
        return entityToDTO(saveUser);
    }

    // 유저 jwt 등록 - 오직 7단계 용
    @Override
    public String saveJwt(UserRequestDto requestDto) {
        UserRole role = null;
        String password = passwordEncoder.encode(requestDto.getPassword());
        if ("USER".equals(requestDto.getRole())) {
            role = UserRole.USER;
        } else if ("ADMIN".equals(requestDto.getRole())) {
            role = UserRole.ADMIN;
        }
        User user = dtoToEntity(requestDto);
        user.setPassword(password);
        user.setRole(role);
        userRepository.save(user);

        return jwtUtil.createToken(user.getName(), role);
    }

    // 유저 로그인
    @Override
    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getName(), user.getRole());

        jwtUtil.addJwtToCookie(token, res); // 쿠키를 HTTP 응답에 추가
        return token;
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
