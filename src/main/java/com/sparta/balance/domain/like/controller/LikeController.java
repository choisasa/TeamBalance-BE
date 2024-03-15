package com.sparta.balance.domain.like.controller;

import com.sparta.balance.domain.like.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(name = "Like Controller", description = "좋아요 기능 컨트롤러")
@Slf4j(topic = "좋아요")
@RestController
@RequestMapping("/api")
public class LikeController {

    /* LikeService : 좋아요 기능 서비스 로직
     * 좋아요 기능 API 호출*/
    private final LikeService likeService;
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "밸런스 게임 선택지 좋아요", description = "밸런스 게임 선택지를 선택해서 좋아요를 추가/취소")
    @ApiResponse(responseCode = "200", description = "선택지에 대한 좋아요 추가/취소 성공")
    @PostMapping("/game/{gameId}/choices/{choiceId}/likes")
    public ResponseEntity<String> likeChoice(@PathVariable Long gameId, @PathVariable Long choiceId) {

        String message = likeService.likeChoice(gameId, choiceId);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Operation(summary = "밸런스 게임 댓글 좋아요", description = "밸런스 게임 댓글에 대한 좋아요 추가/취소")
    @ApiResponse(responseCode = "200", description = "댓글에 대한 좋아요 추가/취소 성공")
    @PostMapping("/game/{gameId}/comment/{commentId}/likes")
    public ResponseEntity<String> likeComment(@PathVariable Long gameId, @PathVariable Long commentId) {

        String message = likeService.likeComment(gameId, commentId);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
