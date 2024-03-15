package com.sparta.balance.domain.like.entity;

import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Tag(name = "Choice Like Table", description = "선택지에 대한 좋아요 테이블")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "choice_likes")
public class ChoiceLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "choice_id", nullable = false)
    private Choice choice;

    @Builder
    public ChoiceLike(User user, Choice choice) {
        this.user = user;
        this.choice = choice;
    }
}
