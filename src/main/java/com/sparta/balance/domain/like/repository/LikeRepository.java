package com.sparta.balance.domain.like.repository;

import com.sparta.balance.domain.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
