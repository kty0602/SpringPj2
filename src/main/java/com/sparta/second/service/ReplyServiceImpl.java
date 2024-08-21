package com.sparta.second.service;

import com.sparta.second.dto.ReplyRequestDto;
import com.sparta.second.dto.ReplyResponseDto;
import com.sparta.second.entity.Reply;
import com.sparta.second.entity.Task;
import com.sparta.second.exception.AlreadyDeleteException;
import com.sparta.second.exception.NotFoundException;
import com.sparta.second.repository.ReplyRepository;
import com.sparta.second.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;
    private final TaskRepository taskRepository;

    // 댓글 등록
    @Override
    public ReplyResponseDto save(ReplyRequestDto requestDto) {
        // 해당 일정이 존재하는지 확인
        taskRepository.checkTaskByTaskId(requestDto.getTaskId()).orElseThrow(() -> new NotFoundException("해당 일정이 존재하지 않습니다."));

        Reply reply = dtoToEntity(requestDto);
        Reply saveReply = replyRepository.save(reply);
        return entityToDTO(saveReply, reply.getTask());
    }

    // 댓글 전체 조회
    @Override
    public List<ReplyResponseDto> getList() {
        List<Reply> replies = replyRepository.findAllActiveReplies();
        return replies.stream().map(reply ->
            entityToDTO(reply, reply.getTask())).collect(Collectors.toList());
    }

    // 댓글 단건 조회
    @Override
    public ReplyResponseDto get(Long replyId) {
        Optional<Reply> reply = replyRepository.getReplyByReplyId(replyId);
        Reply getReply = reply.orElseThrow(() -> new NotFoundException("해당 댓글이 존재하지 않거나 이미 삭제된 댓글입니다."));
        return entityToDTO(getReply, getReply.getTask());
    }

    // 댓글 수정
    @Transactional
    @Override
    public ReplyResponseDto modify(Long replyId, ReplyRequestDto requestDto) {
        Reply reply = replyRepository.getReferenceById(replyId);

        if(requestDto.getContents() != null) {
            reply.changeContent(requestDto.getContents());
        }
        if(requestDto.getName() != null) {
            reply.changeName(requestDto.getName());
        }
        Reply newReply = replyRepository.save(reply);
        return entityToDTO(newReply, reply.getTask());
    }

    // 댓글 삭제
    @Transactional
    @Override
    public void delete(Long replyId) {
        if(replyRepository.isDelete(replyId)) {
            throw new AlreadyDeleteException("해당 일정이 없습니다.");
        } else {
            replyRepository.delete(replyId);
        }
    }
}
