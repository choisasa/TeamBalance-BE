package com.sparta.balance.domain.game.repository;

import com.sparta.balance.domain.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
