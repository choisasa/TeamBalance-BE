package com.sparta.balance.domain.game.repository;

import com.sparta.balance.domain.game.entity.Choice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoiceRepository extends JpaRepository<Choice, Long> {
}
