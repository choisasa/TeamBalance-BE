package com.sparta.balance.domain.comment.repository;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByGameIdAndId(Long gameId, Long commentId);
    List<Comment> findByGameId(Long gameId);
}
