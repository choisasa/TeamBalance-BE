package com.sparta.balance.domain.game.dto;

import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.game.entity.Game;
import com.sparta.balance.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GameResponseDto {

    private Long id;
    private String gameTitle;
//    private List<String> choices;
    private String choiceA;
    private String choiceB;

    @Builder
    public GameResponseDto(Long id, String gameTitle,  List<Choice> choices) {
        this.id = id;
        this.gameTitle = gameTitle;
        if (choices != null && choices.size() >= 2) {
            this.choiceA = choices.get(0).getContent();
            this.choiceB = choices.get(1).getContent();
        }
//        this.choiceA = choiceA;
//        this.choiceB = choiceB;
//        this.choices = choices.stream()
//                .map(Choice::getContent)
//                .collect(Collectors.toList());
    }



}
