package com.sparta.balance.domain.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {
    private String username;
    private String content;

    @Builder
    public CommentResponseDto(String username, String content){
        this.username = username;
        this.content = content;
    }
}
