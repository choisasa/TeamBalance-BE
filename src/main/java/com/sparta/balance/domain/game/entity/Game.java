package com.sparta.balance.domain.game.entity;

import com.sparta.balance.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Tag(name = "game entity", description = "게임 엔티티" )
@Entity
@Getter
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "게임 생성 ID")
    private Long id;

    @Column
    @NotNull(message = "게임의 제목이 비어있습니다.")
    @Schema(description = "게임 제목", nullable = false, example = "짜장면 vs 짬뽕")
    private String gameTitle;

    @Column(length = 1000)
    @NotNull(message = "선택지가 비어있습니다.")
    @Schema(description = "선택지 A", nullable = false, example = "짜장면")
    private String choiceA;

    @Column(length = 1000)
    @NotNull(message = "선택지가 비어있습니다.")
    @Schema(description = "선택지 B", nullable = false, example = "짬뽕")
    private String choiceB;
}
