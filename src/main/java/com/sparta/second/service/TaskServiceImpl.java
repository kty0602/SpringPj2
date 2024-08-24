package com.sparta.second.service;

import com.sparta.second.dto.PageRequestDto;
import com.sparta.second.dto.PageResultDto;
import com.sparta.second.exception.AlreadyDeleteException;
import com.sparta.second.exception.NotFoundException;
import com.sparta.second.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.sparta.second.dto.TaskResponseDto;
import com.sparta.second.dto.TaskRequestDto;
import com.sparta.second.entity.Task;
import com.sparta.second.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final ReplyRepository replyRepository;

    // 일정 등록
    @Override
    public TaskResponseDto save(TaskRequestDto requestDto) {
        Task task = dtoToEntity(requestDto);
        Task saveTask = taskRepository.save(task);
        return entityToDTO(saveTask, 0L);
    }

    // 일정 조회
    @Override
    public TaskResponseDto get(Long taskId) {
        return taskRepository.getTaskByTaskId(taskId)
                .map(result -> {
                    Object[] arr = (Object[]) result;
                    return entityToDTO((Task) arr[0], (Long) arr[1]);
                })
                .orElseThrow(() -> new NotFoundException("해당 일정이 존재하지 않거나 이미 삭제된 일정입니다."));
    }

    // 일정 전체 조회
    @Override
    public PageResultDto<TaskResponseDto, Object[]> getList(PageRequestDto pageRequestDto) {
        Function<Object[], TaskResponseDto> fn = (en -> entityToDTO((Task)en[0], (Long)en[1]));
        Page<Object[]> result = taskRepository.getTaskWithReplyCount(pageRequestDto.getPageable(Sort.by("modDate").descending()));
        return new PageResultDto<>(result, fn);
    }

    @Transactional
    @Override
    public TaskResponseDto modify(Long taskId, TaskRequestDto requestDto) {
        Task task = taskRepository.getReferenceById(taskId);

        if (requestDto.getTitle() != null) {
            task.changeTitle(requestDto.getTitle());
        }
        if (requestDto.getContents() != null) {
            task.changeContent(requestDto.getContents());
        }
        if (requestDto.getName() != null) {
            task.changeName(requestDto.getName());
        }

        Task newTask = taskRepository.save(task);

        // 리턴되는 response값 댓글 수 올바르게 가져오기 위함 <- 크게 의미 없는 코드 없으면 0으로 임시 대체 해야함
        Long replyCount = taskRepository.getTaskByTaskId(taskId)
                .map(result -> (Long) ((Object[]) result)[1])
                .orElse(0L);

        return entityToDTO(newTask, replyCount);
    }

    // 일정 삭제
    @Transactional
    @Override
    public void delete(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AlreadyDeleteException("해당 일정이 없습니다."));
        task.setDeleteStatus(true);
        task.getReplyList().forEach(reply -> reply.setDeleteStatus(true));
    }
}
