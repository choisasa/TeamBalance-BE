package com.sparta.balance.domain.like.service;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.comment.repository.CommentRepository;
import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.game.entity.Game;
import com.sparta.balance.domain.game.repository.ChoiceRepository;
import com.sparta.balance.domain.game.repository.GameRepository;
import com.sparta.balance.domain.like.entity.ChoiceLike;
import com.sparta.balance.domain.like.entity.CommentLike;
import com.sparta.balance.domain.like.repository.ChoiceLikeRepository;
import com.sparta.balance.domain.like.repository.CommentLikeRepository;
import com.sparta.balance.domain.user.entity.User;
import com.sparta.balance.domain.user.repository.UserRepository;
import com.sparta.balance.global.handler.exception.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.balance.global.handler.exception.ErrorCode.GAME_ID_NOT_FOUND;
import static com.sparta.balance.global.handler.exception.ErrorCode.NOT_MATCH_MEMBER_ACCOUNT;

@Slf4j(topic = "좋아요 서비스 로직")
@Service
public class LikeService {

    /*
    * 게임 선택지에 대한 좋아요 기능,
    * 댓글에 대한 좋아요 기능 구현
    * likeRepository : 좋아요 데이터 관리
    * gameRepository : 게임 데이터 확인
    * commentRepository : 댓글 데이터 확인*/
    private final ChoiceLikeRepository choiceLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final GameRepository gameRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ChoiceRepository choiceRepository;
    public LikeService(ChoiceLikeRepository choiceLikeRepository, CommentLikeRepository commentLikeRepository, GameRepository gameRepository,
                       CommentRepository commentRepository, UserRepository userRepository,
                       ChoiceRepository choiceRepository) {
        this.choiceLikeRepository = choiceLikeRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.gameRepository = gameRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.choiceRepository = choiceRepository;
    }
    
    @Transactional
    public void likeChoice(Long gameId, Long choiceId) {
        /*
         * 유저 정보, 게임 정보, 게임 선택지 정보 확인 후 좋아요 정보 저장
         * 에러 발생 시 rollback*/
        /*사용자 검증*/
        User user = getAuthenticatedUser();

        /*게임 검증*/
        Game game = getGameById(gameId);

        /*선택지 검증*/
        Choice choice = choiceRepository.findById(choiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid choice ID: " + choiceId));
        if (!choice.getGame().getId().equals(gameId)) {
            throw new IllegalArgumentException("Choice does not belong to the specified game");
        }

        /*좋아요 추가(취소)*/
        Optional<ChoiceLike> existingLike = choiceLikeRepository.findByUserAndChoice(user, choice);
        if (existingLike.isPresent()) {
            // 좋아요가 이미 있으면 삭제
            choiceLikeRepository.delete(existingLike.get());
        } else {
            // 좋아요가 없으면 추가
            ChoiceLike newLike = new ChoiceLike(user, choice);
            choiceLikeRepository.save(newLike);
        }
    }

    @Transactional
    public void likeComment(Long gameId, Long commentId) {
        /*유저 정보, 게임 정보, 댓글 정보 확인 후 좋아요 정보 저장
        * 에러 발생 시 rollback*/
        /*사용자 검증*/
        User user = getAuthenticatedUser();

        /*게임 검증*/
        Game game = getGameById(gameId);

        /*댓글 검증*/
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comment ID: " + commentId));
        if (!comment.getGame().getId().equals(gameId)) {
            throw new IllegalArgumentException("Comment does not belong to the specified game");
        }

        /*좋아요 추가(취소)*/
        Optional<CommentLike> existingLike = commentLikeRepository.findByUserAndComment(user, comment);
        if (existingLike.isPresent()) {
            // 좋아요가 이미 있으면 삭제
            commentLikeRepository.delete(existingLike.get());
        } else {
            // 좋아요가 없으면 추가
            CommentLike newLike = new CommentLike(user, comment);
            commentLikeRepository.save(newLike);
        }
    }

    /*좋아요 기능 메서드
    * 좋아요 없으면 추가 있으면 취소(삭제)
    * choiceLike, commentLike 메서드*/



    /*사용자 검증 메서드*/
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomApiException(NOT_MATCH_MEMBER_ACCOUNT.getMessage()));
    }

    /*게임 검증 메서드*/
    private Game getGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomApiException(GAME_ID_NOT_FOUND.getMessage()));
    }

//    /*선택지 검증 메서드*/
//    private Choice getChoiceById(Long choiceId) {
//        return choiceRepository.findById(choiceId)
//                .orElseThrow(() -> new CustomApiException(CHOICE_ID_NOT_FOUND.getMessage()));
//    }
//
//    /*댓글 검증 메서드*/
//    private Comment getCommentById(Long commentId) {
//        return commentRepository.findById(commentId)
//                .orElseThrow(() -> new CustomApiException(COMMENT_ID_NOT_FOUND.getMessage()));
//    }
}
