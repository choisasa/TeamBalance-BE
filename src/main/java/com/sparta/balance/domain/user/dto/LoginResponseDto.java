package com.sparta.balance.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponseDto {
    private String username;
    private String refreshToken;
    private String message;

    @Builder
    public LoginResponseDto(String username, String refreshToken, String message) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.message = message;
    }
}
