package com.sparta.balance.domain.like.service;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.comment.repository.CommentRepository;
import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.game.entity.Game;
import com.sparta.balance.domain.game.repository.ChoiceRepository;
import com.sparta.balance.domain.game.repository.GameRepository;
import com.sparta.balance.domain.like.entity.Like;
import com.sparta.balance.domain.like.repository.LikeRepository;
import com.sparta.balance.domain.user.entity.User;
import com.sparta.balance.domain.user.repository.UserRepository;
import com.sparta.balance.global.handler.exception.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.sparta.balance.global.handler.exception.ErrorCode.*;

@Slf4j(topic = "좋아요 서비스 로직")
@Service
public class LikeService {

    /*
    * 게임 선택지에 대한 좋아요 기능,
    * 댓글에 대한 좋아요 기능 구현
    * likeRepository : 좋아요 데이터 관리
    * gameRepository : 게임 데이터 확인
    * commentRepository : 댓글 데이터 확인*/
    private final LikeRepository likeRepository;
    private final GameRepository gameRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ChoiceRepository choiceRepository;
    public LikeService(LikeRepository likeRepository, GameRepository gameRepository,
                       CommentRepository commentRepository, UserRepository userRepository,
                       ChoiceRepository choiceRepository) {
        this.likeRepository = likeRepository;
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
                .orElseThrow(() -> new CustomApiException(CHOICE_ID_NOT_FOUND.getMessage()));

        /*좋아요 추가
        * 있으면 취소(삭제) 없으면 추가*/
        Optional<Like> existingLike = likeRepository.findByGameAndChoiceAndUser(game, choice, user);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like newLike = new Like(user, choice, null);
            likeRepository.save(newLike);
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
                .orElseThrow(() -> new CustomApiException(COMMENT_ID_NOT_FOUND.getMessage()));

        /*좋아요 추가
         * 있으면 취소(삭제) 없으면 추가*/
        Optional<Like> existingLike = likeRepository.findByGameAndCommentAndUser(game, comment, user);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
        } else {
            Like newLike = new Like(user, null, comment);
            likeRepository.save(newLike);
        }

    }

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
}
