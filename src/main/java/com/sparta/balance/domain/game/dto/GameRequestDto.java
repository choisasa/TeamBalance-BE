package com.sparta.balance.domain.game.dto;

import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.user.entity.User;
import lombok.Getter;

@Getter
public class GameRequestDto {

    private String gameTitle;
    private String choiceA;
    private String choiceB;

}
