package com.sparta.balance.domain.game.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.balance.domain.like.entity.ChoiceLike;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "choice entity", description = "밸런스 게임 선택지")
@Entity
@Getter
@NoArgsConstructor
@Table(name = "choice")
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    @NotNull(message = "선택지가 비어있습니다.")
    @Schema(description = "선택지", nullable = false, example = "짜장면")
    private String content;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @JsonIgnore
    @OneToMany(mappedBy = "choice")
    private List<ChoiceLike> likes = new ArrayList<>();

    @Builder
    public Choice(String content, Game game) {
        this.content = content;
        this.game = game;
    }
}