package com.sparta.balance.domain.game.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "game")
    private List<Choice> choices = new ArrayList<>();
}
