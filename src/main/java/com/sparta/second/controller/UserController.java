package com.sparta.second.controller;

import com.sparta.second.dto.ReplyRequestDto;
import com.sparta.second.dto.ReplyResponseDto;
import com.sparta.second.dto.UserRequestDto;
import com.sparta.second.dto.UserResponseDto;
import com.sparta.second.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;

    /*
    * 유저 등록
    *
    * @param UserRequestDto
    * @return UserResponseDto
    * */
//    @PostMapping()
//    public ResponseEntity<UserResponseDto> saveUser(@RequestBody UserRequestDto requestDto) {
//        UserResponseDto responseDto = userService.save(requestDto);
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

    /*
     * 유저 jwt등록
     *
     * @param UserRequestDto
     * @return token
     * */
    @PostMapping()
    public ResponseEntity<String> saveUser(@RequestBody UserRequestDto requestDto) {
        // JWT 토큰을 생성
        String token = userService.saveJwt(requestDto);

        // 토큰을 응답으로 반환
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    /*
     * 유저 조회
     *
     * @param userId
     * @return UserResponseDto
     * */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getReply(@PathVariable("userId") Long userId) {
        UserResponseDto responseDto = userService.get(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 유저 전체 목록 조회
     *
     * @param X
     * @return List<UserResponseDto>
     * */
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getListReply() {
        List<UserResponseDto> responseDto = userService.getList();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 유저 수정
     *
     * @param {userId}
     * @return UserResponseDto
     * */
    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> modify(@PathVariable("userId") Long userId, @RequestBody UserRequestDto requestDto) {
        UserResponseDto responseDto = userService.modify(userId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 유저 삭제
     *
     * @param userId
     * @return String
     * */
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId) {
        userService.delete(userId);
        return new ResponseEntity<>("성공적으로 삭제가 되었습니다.", HttpStatus.OK);
    }
}
