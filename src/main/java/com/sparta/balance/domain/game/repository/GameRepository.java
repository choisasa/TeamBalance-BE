package com.sparta.balance.domain.game.repository;

import com.sparta.balance.domain.game.entity.Game;
import com.sparta.balance.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long> {

    @EntityGraph(attributePaths = "choices")
    Optional<Game> findByIdAndUser(Long id, User user);
}
