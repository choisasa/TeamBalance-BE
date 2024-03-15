package com.sparta.balance.global.service;

import com.sparta.balance.domain.user.entity.UserRoleEnum;
import com.sparta.balance.global.entity.RefreshToken;
import com.sparta.balance.global.jwt.JwtUtil;
import com.sparta.balance.global.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Slf4j(topic = "리프레시 토큰 관리")
@Service
@Transactional
public class RefreshTokenService {

    /*리프레시 토큰 관리 로직
    * 토큰 생성, 저장, 검증, 삭제, 액세스 토큰 갱신 기능 수행*/
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    /*리프레시 토큰 생성 및 저장*/
    public void createAndSaveRefreshToken(String email, String refreshTokenString) {
        Date expiryDate = new Date(System.currentTimeMillis() + JwtUtil.REFRESH_TOKEN_VALIDITY_MS);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenString)
                .email(email)
                .expiryDate(expiryDate)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    /*리프레시 토큰 검증*/
    public boolean validateRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .map(RefreshToken::getExpiryDate)
                .map(expiryDate -> !expiryDate.before(new Date()))
                .orElse(false);
    }

    /*리프레시 토큰으로 새 액세스 토큰 발급*/
    public Optional<String> refreshAccessToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken)
                .filter(token -> !token.getExpiryDate().before(new Date())) /*토큰 만료 여부 검사*/
                .map(RefreshToken::getEmail)
                .map(email -> jwtUtil.createAccessToken(email, UserRoleEnum.USER)); /*새 액세스 토큰 생성*/
    }

    /*리프레시 토큰 삭제*/
    public void deleteRefreshToken(String refreshTokenString) {
        refreshTokenRepository.findByToken(refreshTokenString).ifPresent(refreshToken -> {
            try {
                log.info("Deleting refresh token: {}", refreshToken.getToken());
                refreshTokenRepository.delete(refreshToken);
                /* 영속성 컨텍스트의 변경사항을 데이터베이스에 즉시 반영하는 것을 보장합니다.*/
                refreshTokenRepository.flush();
                log.info("Deleted refresh token: {}", refreshToken.getToken());
            } catch (Exception e) {
                /*예외 발생시 로그에 기록합니다.*/
                log.error("An error occurred while deleting the refresh token: {}", e.getMessage(), e);
            }
        });
    }
<<<<<<< HEAD
}
