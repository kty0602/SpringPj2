package com.sparta.second.filter;

import com.sparta.second.entity.User;
import com.sparta.second.entity.UserRole;
import com.sparta.second.jwt.JwtUtil;
import com.sparta.second.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j(topic = "AuthFilter")
@Component
@Order(2)
@RequiredArgsConstructor
public class AuthFilter implements Filter {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String url = httpServletRequest.getRequestURI();

        try {
            if (url.startsWith("/user") || url.startsWith("/user/login")) {
                // 회원가입, 로그인 관련 API 는 인증 필요없이 요청 진행
                chain.doFilter(request, response); // 다음 Filter 로 이동
            } else {
                // 나머지 API 요청은 인증 처리 진행
                // 토큰 확인
                String tokenValue = jwtUtil.getTokenFromRequest(httpServletRequest);

                if (StringUtils.hasText(tokenValue)) { // 토큰이 존재하면 검증 시작
                    // JWT 토큰 substring
                    String token = jwtUtil.substringToken(tokenValue);

                    // 토큰 검증
                    if (!jwtUtil.validateToken(token)) {
                        throw new IllegalArgumentException("Token Error");
                    }

                    // 토큰에서 사용자 정보 가져오기
                    Claims info = jwtUtil.getUserInfoFromToken(token);

                    User user = userRepository.findByName(info.getSubject()).orElseThrow(() ->
                            new NullPointerException("Not Found User")
                    );

                    /*
                    * 현재 로그인한 사용자의 권한 내용을 가져와서 검사 -> 일정 삭제, 수정 시 확인
                    * 필터에서는 DispatcherServlet에 도달하기 전에 먼저 거쳐지는 곳이여서 @ControllerAdvice에서 예외처리를 해줄 수 없다.
                    * 그리하여 필터에서 발생한 예외를 직접 처리하여 HttpServletResponse에 적절한 상태 코드와 메시지를 설정한다.
                    * */
                    if((url.startsWith("/task/modify") || url.startsWith("/task/delete")) && !user.getRole().equals(UserRole.ADMIN)) {
                        throw new SecurityException("권한이 없습니다.");
                    }

                    request.setAttribute("user", user);
                    chain.doFilter(request, response); // 다음 Filter 로 이동
                } else {
                    throw new IllegalArgumentException("Not Found Token");
                }
            }
        } catch (SecurityException e) {
            httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }
}
