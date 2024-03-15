package com.sparta.balance.domain.comment.service;

import com.sparta.balance.domain.comment.dto.CommentRequestDto;
import com.sparta.balance.domain.comment.dto.CommentResponseDto;
import com.sparta.balance.domain.comment.entity.Comment;
import com.sparta.balance.domain.comment.repository.CommentRepository;
import com.sparta.balance.domain.game.entity.Game;
import com.sparta.balance.domain.game.repository.GameRepository;
import com.sparta.balance.domain.user.entity.User;
import com.sparta.balance.domain.user.repository.UserRepository;
import com.sparta.balance.global.handler.exception.CustomApiException;
import com.sparta.balance.global.handler.exception.CustomValidationException;
import com.sparta.balance.global.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final JwtUtil jwtUtil;

    /**
     * Adds a new comment for a specific game.
     *
     * @param gameId            The ID of the game for which the comment is added.
     * @param commentRequestDto The DTO containing the content of the comment to be added.
     * @param userDetails       The details of the authenticated user adding the comment.
     * @return The DTO containing the added comment.
     * @throws RuntimeException if the user or game is not found.
     */

    // 댓글 추가 기능
    @Transactional
    public CommentResponseDto addComment(Long gameId, CommentRequestDto commentRequestDto, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다"));

        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomApiException("게임을 찾을 수 없습니다"));

        Comment comment = new Comment(commentRequestDto.getContent(), user, game);
        comment = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .username(user.getUsername())
                .content(comment.getContent())
                .build();
    }

    // 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComment(Long gameId, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다"));

        List<Comment> comments = commentRepository.findByGameId(gameId);

        List<CommentResponseDto> responseDtos = comments.stream()
                .map(comment -> CommentResponseDto.builder()
                        .username(comment.getUser().getUsername())
                        .content(comment.getContent())
                        .build())
                .collect(Collectors.toList());

        return responseDtos;
    }

    @Transactional
    public CommentResponseDto updateComment(Long gameId, Long commentId, CommentRequestDto commentRequestDto, UserDetails userDetails) {
        // 사용자 정보 확인
        if (userDetails == null) {
            throw new CustomApiException("사용자 정보가 유효하지 않습니다");
        }
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다"));

        // 댓글 존재 확인
        Comment comment = commentRepository.findByGameIdAndId(gameId, commentId)
                .orElseThrow(() -> new CustomApiException("댓글을 찾을 수 없습니다"));

        // 댓글 작성자 확인
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomApiException("이 댓글을 수정할 권한이 없습니다");
        }

        // 댓글 내용 업데이트
        comment.updateContent(commentRequestDto.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return CommentResponseDto.builder()
                .username(user.getUsername())
                .content(updatedComment.getContent())
                .build();
    }

    @Transactional
    public void deleteComment(Long id,Long commentId, UserDetails userDetails) {
        // 유저 id로 댓글 찾기
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomApiException( "댓글을 찾을 수 없습니다."));

        // 유저 이름으로 댓글 찾기
        String username = userDetails.getUsername();
        User currentUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomApiException("사용자를 찾을 수 없습니다."));

        // 예외
        if (!comment.getUser().equals(currentUser)) {
            throw new CustomApiException("해당 댓글을 삭제할 수 있는 권한이 없습니다.");
        }
        if (!comment.getGame().getId().equals(id)){
            throw new CustomApiException("지정된 게임에 속하지 않은 댓글은 삭제할 수 없습니다.");
        }

        // 댓글 삭제
        commentRepository.delete(comment);
    }
}
