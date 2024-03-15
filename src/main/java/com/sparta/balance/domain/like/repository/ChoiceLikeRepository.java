package com.sparta.balance.domain.like.repository;

import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.like.entity.ChoiceLike;
import com.sparta.balance.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChoiceLikeRepository extends JpaRepository<ChoiceLike, Long> {
    Optional<ChoiceLike> findByUserAndChoice(User user, Choice choice);
}
