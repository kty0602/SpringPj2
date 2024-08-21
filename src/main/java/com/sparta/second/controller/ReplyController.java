package com.sparta.second.controller;

import com.sparta.second.dto.ReplyRequestDto;
import com.sparta.second.dto.ReplyResponseDto;
import com.sparta.second.service.ReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
@RequiredArgsConstructor
@Log4j2
public class ReplyController {

    private final ReplyService replyService;

    // 댓글 등록
    @PostMapping()
    public ResponseEntity<ReplyResponseDto> saveReply(@RequestBody ReplyRequestDto requestDto) {
        ReplyResponseDto responseDto = replyService.save(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 특정 댓글 조회
    @GetMapping("/{replyId}")
    public ResponseEntity<ReplyResponseDto> getReply(@PathVariable("replyId") Long replyId) {
        ReplyResponseDto responseDto = replyService.get(replyId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 전체 조회
    @GetMapping()
    public ResponseEntity<List<ReplyResponseDto>> getListReply() {
        List<ReplyResponseDto> responseDto = replyService.getList();
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 수정
    @PatchMapping("/{replyId}")
    public ResponseEntity<ReplyResponseDto> modify(@PathVariable("replyId") Long replyId, @RequestBody ReplyRequestDto requestDto) {
        ReplyResponseDto responseDto = replyService.modify(replyId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 댓글 삭제
    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable("replyId") Long replyId) {
        replyService.delete(replyId);
        return new ResponseEntity<>("성공적으로 삭제가 되었습니다.", HttpStatus.OK);
    }
}
