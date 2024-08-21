package com.sparta.second.controller;

import com.sparta.second.dto.TaskRequestDto;
import com.sparta.second.dto.TaskResponseDto;
import com.sparta.second.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Log4j2
public class TaskController {
    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskResponseDto> saveTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.save(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable("taskId") Long taskId) {
        TaskResponseDto responseDto = taskService.get(taskId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> modifyTask(@PathVariable("taskId") Long taskId, @RequestBody TaskRequestDto requestDto) {
        TaskResponseDto responseDto = taskService.modify(taskId, requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
