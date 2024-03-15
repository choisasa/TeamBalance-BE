package com.sparta.balance.domain.game.controller;

import com.sparta.balance.domain.game.dto.GameLikesResponseDto;
import com.sparta.balance.domain.game.dto.GameRequestDto;
import com.sparta.balance.domain.game.dto.GameResponseDto;
import com.sparta.balance.domain.game.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j(topic = "게임 생성, 선택 조회, 전체 조회, 삭제")
@RestController
@RequestMapping("/api")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/game")
    @Operation(summary = "밸런스 게임 생성", description = "밸런스 게임의 제목, 선택지를 등록합니다.")
    @ApiResponse(responseCode = "201", description = "밸런스 게임 생성 완료")
    public ResponseEntity<GameResponseDto> createGame(@RequestBody GameRequestDto gameRequestDto, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.status(HttpStatus.CREATED).body(gameService.createGame(gameRequestDto, userDetails));
    }

    @GetMapping("/game/{id}")
    @Operation(summary = "선택 밸런스 게임 조회", description = "선택한 밸런스 게임을 조회할 수 있습니다.")
    @ApiResponse(responseCode = "201", description = "밸런스 게임 조회 완료")
    public ResponseEntity<GameLikesResponseDto> findGame(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.findGame(id));
    }

    // 최신순으로 정렬(파람필요없음) -> 내림차순
    @GetMapping("/games")
    @Operation(summary = "밸런스 게임 전체 조회", description = "밸런스 게임의 목록을 조회할 수 있습니다.")
    @ApiResponse(responseCode = "201", description = "밸런스 게임 목록 조회 완료")
    public ResponseEntity<List<GameResponseDto>> getGames() {
        return ResponseEntity.status(HttpStatus.OK).body(gameService.getGames());
    }

    @DeleteMapping("game/{id}")
    @Operation(summary = "밸런스 게임 삭제", description = "밸런스 게임을 삭제할 수 있습니다.")
    @ApiResponse(responseCode = "201", description = "밸런스 게임 삭제 완료")
    public ResponseEntity<String> deleteGame(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        gameService.deleteGame(id, userDetails);
        return ResponseEntity.ok("게임이 삭제되었습니다.");
    }
}

