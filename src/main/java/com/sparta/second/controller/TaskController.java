package com.sparta.second.controller;

import com.sparta.second.dto.*;
import com.sparta.second.entity.Task;
import com.sparta.second.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Log4j2
public class TaskController {
    private final TaskService taskService;

    /*
     * 일정 등록
     *
     * @param TaskRequestDto
     * @return TaskResponseDto
     * */
    @PostMapping()
    public ResponseEntity<TaskResponseDto> saveTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.save(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 일정 단건 조회
     *
     * @param taskId
     * @return TaskResponseDto
     * */
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("taskId") Long taskId) {
        TaskResponseDto responseDto = taskService.get(taskId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 일정 전체 조회
     *
     * @param X
     * @return PageResultDto<TaskResponseDto, Object[]>
     * */
    @GetMapping()
    public ResponseEntity<PageResultDto<TaskListResponseDto, Object[]>> getAllList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequestDto pageRequestDto = new PageRequestDto(page, size);
        return new ResponseEntity<>(taskService.getList(pageRequestDto), HttpStatus.OK);
    }

    /*
     * 일정 수정
     * [수정 사항]
     * AuthFilter에서 수정과 삭제만 권한을 검사해야하는데 기존코드로 진행 시
     * 일정 단건 조회에서도 권한을 검사할 수 있어서 rest가 깨지는 한이 있어도 행위를 포함시키는 방향으로 수정
     *
     * @param taskId
     * @return TaskResponseDto
     * */
    @PatchMapping("/modify/{taskId}")
    public ResponseEntity<TaskResponseDto> modifyTask(@PathVariable("taskId") Long taskId, @RequestBody TaskRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.modify(taskId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /*
     * 일정 삭제
     * [수정 사항]
     * AuthFilter에서 수정과 삭제만 권한을 검사해야하는데 기존코드로 진행 시
     * 일정 단건 조회에서도 권한을 검사할 수 있어서 rest가 깨지는 한이 있어도 행위를 포함시키는 방향으로 수정
     *
     * @param taskId
     * @return String
     * */
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<String> deleteReply(@PathVariable("taskId") Long taskId) {
        taskService.delete(taskId);
        return new ResponseEntity<>("성공적으로 삭제가 되었습니다.", HttpStatus.OK);
    }


}
