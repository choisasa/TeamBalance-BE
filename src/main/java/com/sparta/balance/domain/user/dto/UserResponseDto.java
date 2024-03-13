package com.sparta.balance.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
    @NotBlank
    private String username;
    private String token;
    private String type = "Bearer";
    @Builder
    public UserResponseDto(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
