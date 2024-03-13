package com.sparta.balance.domain.like.entity;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeId implements Serializable {
    /*
    * user : 사용자 ID
    * choice : 선택지 ID, 게임의 선택지에 대한 좋아요
    * comment : 댓글 ID, 댓글에 대한 좋아요*/
    @EqualsAndHashCode.Include
    private Long user;
    @EqualsAndHashCode.Include
    private Long choice;
    @EqualsAndHashCode.Include
    private Long comment;
}
