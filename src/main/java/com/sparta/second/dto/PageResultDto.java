package com.sparta.second.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDto<DTO, EN> {
    private List<DTO> dtoList;

    // 총 페이지
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    // 페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDto(Page<EN> result, Function<EN, DTO> fn) {
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 1페이지가 defaultValue로 인해 0부터 시작하므로 1 추가
        this.size = pageable.getPageSize();

        /*
        * 현재 페이지를 기준으로 페이지 목록의 끝 페이지를 계산
        * 현제 페이지를 10으로 나눈 후 올림하여 페이지 범위를 결정
        * 도서 참고 : 코드로 배우는 스프링부트 웹 프로젝트
        * */
        int tempEnd = (int)(Math.ceil(page/10.0))*10;
        /* Ex) 현 페이지가 15일 때 tempEnd는 20이므로 start페이지 넘버는 11이 된다.*/
        start = tempEnd - 9;
        end = totalPage > tempEnd ? tempEnd : totalPage;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }

}
