package com.sparta.balance.domain.like.entity;

import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.game.entity.Game;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "like")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Schema(description = "좋아요 ID")
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "choice_id")
    private Choice choice;
}
