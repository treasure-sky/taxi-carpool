package edu.kangwon.university.taxicarpool.exception;

import edu.kangwon.university.taxicarpool.auth.authException.AuthenticationFailedException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import edu.kangwon.university.taxicarpool.email.exception.EmailSendFailedException;
import edu.kangwon.university.taxicarpool.email.exception.EmailVerificationNotFoundException;
import edu.kangwon.university.taxicarpool.email.exception.ExpiredVerificationCodeException;
import edu.kangwon.university.taxicarpool.email.exception.InvalidVerificationCodeException;
import edu.kangwon.university.taxicarpool.map.exception.KakaoApiParseException;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedNicknameException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyAlreadyDeletedException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyEmptyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyFullException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.UnauthorizedHostAccessException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PartyNotFoundException.class)
    public ErrorResponseDTO handlePartyNotFoundException(PartyNotFoundException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ErrorResponseDTO handleAuthenticationFailedException(AuthenticationFailedException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
            request.getRequestURI());
    }

    // 빈 JSON이나 잘못된 형식으로 인한 예외 처리
    @ExceptionHandler({MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "이메일과 패스워드 모두 입력해주세요");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ErrorResponseDTO handleTokenInvalidException(TokenInvalidException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponseDTO handleIllegalArgumentException(IllegalArgumentException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponseDTO handleMemberNotFoundException(MemberNotFoundException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(MemberNotInPartyException.class)
    public ErrorResponseDTO handleMemberNotInPartyException(MemberNotInPartyException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(PartyAlreadyDeletedException.class)
    public ErrorResponseDTO handlePartyAlreadyDeletedException(PartyAlreadyDeletedException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.GONE.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(PartyEmptyException.class)
    public ErrorResponseDTO handlePartyEmptyException(PartyEmptyException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.GONE.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(UnauthorizedHostAccessException.class)
    public ErrorResponseDTO handleUnauthorizedHostAccessException(
        UnauthorizedHostAccessException ex, HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResponseDTO handleTokenExpiredException(TokenExpiredException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(PartyFullException.class)
    public ErrorResponseDTO handlePartyFullException(PartyFullException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.CONFLICT.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(DuplicatedNicknameException.class)
    public ErrorResponseDTO handleDuplicatedNicknameException(DuplicatedNicknameException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.CONFLICT.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ErrorResponseDTO handleDuplicatedEmailException(DuplicatedEmailException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.CONFLICT.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(EmailVerificationNotFoundException.class)
    public ErrorResponseDTO handleEmailVerificationNotFoundException(
        EmailVerificationNotFoundException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(EmailSendFailedException.class)
    public ErrorResponseDTO handleEmailSendFailedException(EmailSendFailedException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ErrorResponseDTO handleInvalidVerificationCodeException(
        InvalidVerificationCodeException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(ExpiredVerificationCodeException.class)
    public ErrorResponseDTO handleExpiredVerificationCodeException(
        ExpiredVerificationCodeException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(),
            request.getRequestURI());
    }

    @ExceptionHandler(KakaoApiParseException.class)
    public ErrorResponseDTO handleKakaoApiParseException(
        KakaoApiParseException ex,
        HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
            request.getRequestURI());
    }

    // 그 외 잡히지 않은 모든 예외에 대한 전역 핸들러
    @ExceptionHandler(Exception.class)
    public ErrorResponseDTO handleException(Exception ex, HttpServletRequest request) {
        return new ErrorResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.",
            request.getRequestURI());
    }
}
