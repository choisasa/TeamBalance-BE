package com.sparta.balance.domain.like.repository;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.like.entity.CommentLike;
import com.sparta.balance.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
