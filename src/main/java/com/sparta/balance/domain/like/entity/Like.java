package com.sparta.balance.domain.like.entity;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes")
@IdClass(LikeId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "좋아요를 준 유저 ID")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "choice_id")
    @Schema(description = "좋아요가 달린 선택지 ID")
    private Choice choice;

    @Id
    @ManyToOne
    @JoinColumn(name = "comment_id")
    @Schema(description = "좋아요가 달린 댓글 ID")
    private Comment comment;

    @Builder
    public Like(User user, Choice choice, Comment comment) {
        this.user = user;
        this.choice = choice;
        this.comment = comment;
    }
}
