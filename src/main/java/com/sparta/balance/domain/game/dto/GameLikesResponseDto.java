package com.sparta.balance.domain.game.dto;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.game.entity.Choice;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class GameLikesResponseDto {

    private Long id;
    private String gameTitle;
//    private List<String> choices;
    private String choiceA;
    private String choiceB;

    private List<Comment> comments;
    private Map<Long, Long> choiceLikeCounts;
    private long numberOfCommentLikes;
    private Map<Long, Long> commentLikeCounts;

    @Builder
    public GameLikesResponseDto(Long id, String gameTitle, List<Choice> choices,
                                List<Comment> comments, Map<Long, Long> choiceLikeCounts,
                                long numberOfCommentLikes, Map<Long, Long> commentLikeCounts) {
        this.id = id;
        this.gameTitle = gameTitle;
        if (choices != null && choices.size() >= 2) {
            this.choiceA = choices.get(0).getContent();
            this.choiceB = choices.get(1).getContent();
        }
        this.comments = comments;
        this.choiceLikeCounts = choiceLikeCounts;
        this.numberOfCommentLikes = numberOfCommentLikes;
        this.commentLikeCounts = commentLikeCounts;
    }
}

