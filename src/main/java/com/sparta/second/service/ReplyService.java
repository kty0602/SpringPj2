package com.sparta.second.service;

import com.sparta.second.dto.ReplyRequestDto;
import com.sparta.second.dto.ReplyResponseDto;
import com.sparta.second.entity.Task;
import com.sparta.second.entity.Reply;
import com.sparta.second.entity.User;

import java.util.List;

public interface ReplyService {

    // 댓글 등록
    ReplyResponseDto save(ReplyRequestDto requestDto);

    //댓글 전체
    List<ReplyResponseDto> getList();

    // 댓글 단건 조회
    ReplyResponseDto get(Long replyId);

    // 댓글 수정
    ReplyResponseDto modify(Long replyId, ReplyRequestDto requestDto);

    //댓글 삭제
    void delete(Long replyId);

    default Reply dtoToEntity(ReplyRequestDto dto) {
        Task task = Task.builder().taskId(dto.getTaskId()).build();
        User user = User.builder().userId(dto.getUserId()).build();

        Reply reply = Reply.builder()
                .contents(dto.getContents())
                .user(user)
                .task(task)
                .deleteStatus(false)
                .build();
        return reply;
    }

    default ReplyResponseDto entityToDTO(Reply reply, Task task, User user) {
        ReplyResponseDto dto = ReplyResponseDto.builder()
                .replyId(reply.getReplyId())
                .contents(reply.getContents())
                .userName(user.getName())
                .taskId(task.getTaskId())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
        return dto;
    }
}
