package com.sparta.balance.domain.game.service;

import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.comment.repository.CommentRepository;
import com.sparta.balance.domain.game.dto.GameLikesResponseDto;
import com.sparta.balance.domain.game.dto.GameRequestDto;
import com.sparta.balance.domain.game.dto.GameResponseDto;
import com.sparta.balance.domain.game.entity.Choice;
import com.sparta.balance.domain.game.entity.Game;
import com.sparta.balance.domain.game.repository.ChoiceRepository;
import com.sparta.balance.domain.game.repository.GameRepository;
import com.sparta.balance.domain.like.repository.ChoiceLikeRepository;
import com.sparta.balance.domain.like.repository.CommentLikeRepository;
import com.sparta.balance.domain.user.entity.User;
import com.sparta.balance.domain.user.repository.UserRepository;
import com.sparta.balance.global.handler.exception.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sparta.balance.global.handler.exception.ErrorCode.GAME_ID_NOT_FOUND;

@Slf4j
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ChoiceRepository choiceRepository;
    private final CommentRepository commentRepository;
    private final ChoiceLikeRepository choiceLikeRepository;

    private final CommentLikeRepository commentLikeRepository;


    public GameService(GameRepository gameRepository, UserRepository userRepository, ChoiceRepository choiceRepository, CommentRepository commentRepository, ChoiceLikeRepository choiceLikeRepository, CommentLikeRepository commentLikeRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.choiceRepository = choiceRepository;
        this.commentRepository = commentRepository;
        this.choiceLikeRepository = choiceLikeRepository;
        this.commentLikeRepository = commentLikeRepository;
    }

    /*
     * 밸런스 게임 생성*/
    @Transactional
    public GameResponseDto createGame(GameRequestDto gameRequestDto, UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다."));


        /*builder 를 사용해서 객체 생성 */
        Game game = Game.builder()
                .gameTitle(gameRequestDto.getGameTitle())
                .user(user)
//                .choices(Arrays.asList(choiceA, choiceB))
                .build();

        gameRepository.save(game);


        /*
         * 선택지 생성*/
        Choice choiceA = Choice.builder()
                .content(gameRequestDto.getChoiceA())
                .game(game)
                .build();

        Choice choiceB = Choice.builder()
                .content(gameRequestDto.getChoiceB())
                .game(game)
                .build();


        choiceRepository.save(choiceA);
        choiceRepository.save(choiceB);

//        List<Choice> choices = game.getChoices();

        return GameResponseDto.builder()
                .id(game.getId())
                .gameTitle(game.getGameTitle())
//                .choices(choices)
//                .choiceA(choiceA.getContent())
//                .choiceB(choiceB.getContent())
                .choices(game.getChoices())
                .build();
    }

    /*
     * 선택한 밸런스 게임 조회*/
    @Transactional
    public GameLikesResponseDto findGame(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new CustomApiException(GAME_ID_NOT_FOUND.getMessage()));
        /*
         * 사용자 정의 예외 클래스 CustomApiException 를 만들었기 때문에 NotFoundException 를 사용할 수 없다. 사용하고 싶으면 사용자 정의 클래스에 추가하면 된다. */

        List<Choice> choices = game.getChoices();

        // 각 선택지의 좋아요 개수 계산
        Map<Long, Long> choiceLikeCounts = choices.stream()
                .collect(Collectors.toMap(
                        Choice::getId,
                        choice -> choiceLikeRepository.countByChoiceId(choice.getId())
                ));

//        // 댓글에 대한 좋아요 개수 계산
//        List<Comment> comments = commentRepository.findByGameId(id);
//        long numberOfCommentLikes = comments.stream()
//                .mapToLong(comment -> commentLikeRepository.countByCommentId(comment.getId()))
//                .sum();

        Map<Long, Long> commentLikeCounts = commentRepository.findByGameId(id)
                .stream()
                .collect(Collectors.toMap(
                        Comment::getId,
                        comment -> commentLikeRepository.countByCommentId(comment.getId())
                ));

        return GameLikesResponseDto.builder()
                .id(id)
                .gameTitle(game.getGameTitle())
                .choices(choices)
                .choiceLikeCounts(choiceLikeCounts)
                .commentLikeCounts(commentLikeCounts)
                .build();
    }
//        List<Choice> choices = new ArrayList<>();
//
//        return new GameResponseDto(id, game.getGameTitle(), choiceA, choiceB);


    // 최신순으로 정렬(파람필요없음) -> 내림차순
    /*
     * 밸런스 게임 조회*/
    @Transactional
    public List<GameResponseDto> getGames() {
        List<Game> games = gameRepository.findAll();/*Sort.by(Sort.Direction.DESC, "createdAt")*/
        return games.stream()
                .map(game -> new GameResponseDto(game.getId(), game.getGameTitle(), game.getChoices()))
                .collect(Collectors.toList());
    }

    /*
     * 선택 밸런스 게임 삭제*/
    @Transactional
    public void deleteGame(Long id, UserDetails userDetails) {

        /*수정전 코드*/
        Game game = gameRepository.findById(id)
                .orElseThrow(()-> new CustomApiException(GAME_ID_NOT_FOUND.getMessage()));

        String findUser = userDetails.getUsername();
        User user = userRepository.findByEmail(findUser)
                .orElseThrow(()->new CustomApiException("사용자를 찾을 수 없습니다."));

        if(!game.getUser().equals(user)){
            throw  new CustomApiException("해당 게임을 삭제할 수 있는 권한이 없습니다.");
        }

        choiceRepository.deleteAll(game.getChoices());
//        User user = userRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(()-> new CustomApiException(NOT_MATCH_MEMBER_ACCOUNT.getMessage()));

//        /* 수정한 코드 */
//        Game game = gameRepository.findByIdAndUser(id, userDetails.)
//                .orElseThrow(() -> new CustomApiException(GAME_ID_NOT_FOUND.getMessage()));

        gameRepository.delete(game);
    }
}


