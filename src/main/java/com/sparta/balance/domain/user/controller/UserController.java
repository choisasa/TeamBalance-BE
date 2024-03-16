package com.sparta.balance.domain.user.controller;

import com.sparta.balance.domain.user.dto.LoginRequestDto;
import com.sparta.balance.domain.user.dto.LoginResponseDto;
import com.sparta.balance.domain.user.dto.SignupRequestDto;
import com.sparta.balance.domain.user.dto.UserResponseDto;
import com.sparta.balance.domain.user.service.UserService;
import com.sparta.balance.global.dto.RefreshTokenRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "회원 가입, 로그인 API 컨트롤러")
@Slf4j(topic = "회원 가입, 로그인")
@RestController
@RequestMapping("/api/user")
public class UserController {

    /*
     * UserService 필드 주입(생성자 사용)*/
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "이메일(아이디), 비밀번호, 유저 이름을 등록합니다.")
    @ApiResponse(responseCode = "201", description = "회원 가입 완료")
    /*회원가입 기능 호출*/
    public ResponseEntity<String> signupUser(@RequestBody SignupRequestDto requestDto) {

        userService.signupUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "회원 이메일(아이디), 비밀번호를 입력해 로그인할 수 있습니다.")
    @ApiResponse(responseCode = "200", description = "로그인 완료")
    /*로그인 기능 호출*/
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody LoginRequestDto requestDto) {
        UserResponseDto userResponseDto = userService.loginUser(requestDto);

        /*엑세스 토큰을 Authorization Header로 반환*/
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, userResponseDto.getAccessToken());

        LoginResponseDto loginResponseDto = new LoginResponseDto(
                userResponseDto.getUsername(), userResponseDto.getRefreshToken(), "로그인에 성공했습니다."
        );

        /*로그인 완료 후  리프레시 토큰, 유저이름, 성공 메시지 반환
        * 실패 시 에러 메시지 반환*/
        return ResponseEntity.ok()
                .headers(headers)
                .body(loginResponseDto);
    }

    /*로그아웃 기능 호출*/
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 시 JWT 토큰을 만료처리 합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 완료")
    public ResponseEntity<String> logoutUser(@RequestBody RefreshTokenRequest request) {
        /*리프레시 토큰이 없으면 badRequest 반환*/
        String refreshTokenString = request.getRefreshToken();
        if (refreshTokenString == null || refreshTokenString.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        /*로그아웃 API 호출*/
        userService.logoutUser(refreshTokenString);
        /*로그아웃 메시지 반환*/
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 되었습니다.");
    }
}

