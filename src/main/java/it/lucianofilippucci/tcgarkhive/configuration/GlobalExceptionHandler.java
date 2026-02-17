package it.lucianofilippucci.tcgarkhive.configuration;

import it.lucianofilippucci.tcgarkhive.API.V1.DTO.HttpResponse;
import it.lucianofilippucci.tcgarkhive.entity.TCGListEntry;
import it.lucianofilippucci.tcgarkhive.helpers.enums.ErrorCodes;
import it.lucianofilippucci.tcgarkhive.helpers.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotExistsException.class)
    public ResponseEntity<HttpResponse<String>> handleUserNotExists(UserNotExistsException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.<String>builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .errorCode(ErrorCodes.USER_NOT_FOUND)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(RoleNotExistsException.class)
    public ResponseEntity<HttpResponse<String>> handleRoleNotExists(RoleNotExistsException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.<String>builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .errorCode(ErrorCodes.ROLE_NOT_FOUND)
                        .build()
        );
    }

    @ExceptionHandler(TCGNotFoundException.class)
    public ResponseEntity<HttpResponse<String>> handleTCGNotFound(TCGNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.<String>builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .errorCode(ErrorCodes.TCG_NOT_FOUND_ERROR)
                        .build()
        );
    }

    @ExceptionHandler(CardRarityNotFoundException.class)
    public ResponseEntity<HttpResponse<String>> handleCardRarityNotFound(CardRarityNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                HttpResponse.<String>builder()
                        .timestamp(LocalDateTime.now())
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .errorCode(ErrorCodes.CARD_RARITY_NOT_FOUND)
                        .build()
        );
    }


    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<HttpResponse<String>> handleInternalError(InternalErrorException ex, HttpServletRequest req) {
        //TODO: Log the error -> this involves the setup of logging systems + Grafana + Grafana Alloy
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }


    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<HttpResponse<String>> handleCardNotFound(CardNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        HttpResponse.<String>builder()
                                .timestamp(LocalDateTime.now())
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .errorCode(ErrorCodes.CARD_NOT_FOUND)
                                .build()
                );
    }

    @ExceptionHandler(UserTCGListNotFoundException.class)
    public ResponseEntity<HttpResponse<String>> handleUserTcgListNotFound(UserTCGListNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        HttpResponse.<String>builder()
                                .timestamp(LocalDateTime.now())
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .errorCode(ErrorCodes.USER_TCG_LIST_NOT_FOUND)
                                .data(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<HttpResponse<String>> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        HttpResponse.<String>builder()
                                .timestamp(LocalDateTime.now())
                                .statusCode(HttpStatus.UNAUTHORIZED.value())
                                .errorCode(ErrorCodes.UNAUTHORIZED_ACCESS)
                                .data(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(TCGListentryNotFound.class)
    public ResponseEntity<HttpResponse<String>> handleTCGListEntryNotFound(TCGListentryNotFound ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        HttpResponse.<String>builder()
                                .timestamp(LocalDateTime.now())
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .errorCode(ErrorCodes.TCG_LIST_ENTRY_NOT_FOUND)
                                .data(ex.getMessage())
                                .build()
                );
    }

}
