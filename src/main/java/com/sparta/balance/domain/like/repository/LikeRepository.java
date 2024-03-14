package com.sparta.balance.domain.like.repository;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.like.entity.Like;
import com.sparta.balance.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByChoiceAndUser(Choice choice, User user);
    Optional<Like> findByCommentAndUser(Comment comment, User user);
}
