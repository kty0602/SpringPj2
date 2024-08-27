package com.sparta.second.service;

import com.sparta.second.dto.*;
import com.sparta.second.entity.User;
import com.sparta.second.exception.AlreadyDeleteException;
import com.sparta.second.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.sparta.second.entity.Task;
import com.sparta.second.repository.TaskRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final RestTemplate restTemplate;

    public TaskServiceImpl(TaskRepository taskRepository, RestTemplateBuilder builder) {
        this.taskRepository = taskRepository;
        this.restTemplate = builder.build();
    }

    // 일정 등록
    @Override
    public TaskResponseDto save(TaskRequestDto requestDto) {

        // 현재 날짜 구하기
        LocalDate day = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        String today = day.format(formatter);

        // getCallObject 호출
        String weatherData = getCallObject(today);

        if (weatherData == null) {
            throw new NotFoundException("해당 날짜 데이터가 없습니다.");
        }

        Task task = dtoToEntity(requestDto);
        Task saveTask = taskRepository.save(task);
        saveTask.setWeather(weatherData);


        return entityToDTO(saveTask, saveTask.getUser(),0L);
    }

    public String getCallObject(String date) {
        // 요청 URL 만들기
        // 요청받는 쪽에서 queryParam을 통해서 보내도 필터링하는 코드가 없는 것 같다. -> 전체 데이터 목록을 가져오고 있음
        // 참고 : https://velog.io/@back7418/Java-JSON%EC%9D%84-%ED%8C%8C%EC%8B%B1%ED%95%98%EB%8A%94-%EA%B0%80%EC%9E%A5-%EC%89%AC%EC%9A%B4-%EB%B0%A9%EB%B2%95
        URI uri = UriComponentsBuilder
                .fromUriString("https://f-api.github.io")
                .path("/f-api/weather.json")
                .encode()
                .build()
                .toUri();
        log.info("uri = " + uri);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String jsonString = response.getBody();

        JSONArray weathers = new JSONArray(jsonString);
        for(int i = 0; i<weathers.length(); i++) {
            JSONObject jsonObject = weathers.getJSONObject(i);
            // 날짜가 일치하는 weather값을 반환한다.
            if(jsonObject.get("date").equals(date)) {
                return jsonObject.getString("weather");
            }
        }
        return null;
    }


    // 일정 조회
    @Override
    public TaskResponseDto get(Long taskId) {
        return taskRepository.getTaskByTaskId(taskId)
                .map(result -> {
                    Object[] arr = (Object[]) result;
                    return entityToDTO((Task) arr[0], (User) arr[1],(Long) arr[2]);
                })
                .orElseThrow(() -> new NotFoundException("해당 일정이 존재하지 않거나 이미 삭제된 일정입니다."));
    }

    // 일정 전체 조회
    @Override
    public PageResultDto<TaskListResponseDto, Object[]> getList(PageRequestDto pageRequestDto) {
        Function<Object[], TaskListResponseDto> fn = (en -> entityToDTO1((Task)en[0], (User)en[1],(Long)en[2]));
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

        Task newTask = taskRepository.save(task);

        // 리턴되는 response값 댓글 수 올바르게 가져오기 위함 <- 크게 의미 없는 코드 없으면 0으로 임시 대체 해야함
        Long replyCount = taskRepository.getTaskByTaskId(taskId)
                .map(result -> (Long) ((Object[]) result)[2])
                .orElse(0L);

        return entityToDTO(newTask, newTask.getUser(), replyCount);
    }

    // 일정 삭제
    @Transactional
    @Override
    public void delete(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AlreadyDeleteException("해당 일정이 없습니다."));
        task.setDeleteStatus(true);
        task.getManagerList().forEach(manager -> manager.setDeleteStatus(true));
        task.getReplyList().forEach(reply -> reply.setDeleteStatus(true));
    }
}
