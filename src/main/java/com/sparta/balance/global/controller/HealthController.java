package com.sparta.balance.global.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class HealthController {

    @Operation(summary = "health check", description = "AWS health check api")
    @GetMapping("/health")
    public ResponseEntity<Void> getHealth() {

        return ResponseEntity.ok().build();
    }
}
