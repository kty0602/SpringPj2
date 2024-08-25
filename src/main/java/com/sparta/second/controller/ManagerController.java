package com.sparta.second.controller;

import com.sparta.second.dto.ManagerRequestDto;
import com.sparta.second.dto.ManagerResponseDto;
import com.sparta.second.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Log4j2
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping()
    public ResponseEntity<List<ManagerResponseDto>> save(@RequestBody List<ManagerRequestDto> managerRequestDtoList) {
        List<ManagerResponseDto> managerResponseDtoList = managerService.save(managerRequestDtoList);
        return new ResponseEntity<>(managerResponseDtoList, HttpStatus.OK);
    }
}
