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
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyAlreadyDeletedException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyEmptyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyFullException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyGetCustomException;
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
    public ResponseEntity<ErrorResponseDTO> handlePartyNotFoundException(PartyNotFoundException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthenticationFailedException(
        AuthenticationFailedException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    // 빈 JSON이나 잘못된 형식으로 인한 예외 처리
    @ExceptionHandler({MethodArgumentNotValidException.class,
        HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> handleValidationExceptions(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "로그인 형식을 올바르게 보내주세요.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenInvalidException(TokenInvalidException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(
        IllegalArgumentException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleMemberNotFoundException(
        MemberNotFoundException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MemberNotInPartyException.class)
    public ResponseEntity<ErrorResponseDTO> handleMemberNotInPartyException(
        MemberNotInPartyException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PartyAlreadyDeletedException.class)
    public ResponseEntity<ErrorResponseDTO> handlePartyAlreadyDeletedException(
        PartyAlreadyDeletedException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.GONE.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.GONE).body(errorResponse);
    }

    @ExceptionHandler(PartyEmptyException.class)
    public ResponseEntity<ErrorResponseDTO> handlePartyEmptyException(PartyEmptyException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.GONE.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.GONE).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedHostAccessException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnauthorizedHostAccessException(
        UnauthorizedHostAccessException ex, HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenExpiredException(TokenExpiredException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(PartyFullException.class)
    public ResponseEntity<ErrorResponseDTO> handlePartyFullException(PartyFullException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedNicknameException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatedNicknameException(
        DuplicatedNicknameException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicatedEmailException(
        DuplicatedEmailException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(EmailVerificationNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailVerificationNotFoundException(
        EmailVerificationNotFoundException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailSendFailedException(
        EmailSendFailedException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidVerificationCodeException(
        InvalidVerificationCodeException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(ExpiredVerificationCodeException.class)
    public ResponseEntity<ErrorResponseDTO> handleExpiredVerificationCodeException(
        ExpiredVerificationCodeException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(HttpStatus.UNAUTHORIZED.value(),
            ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(KakaoApiParseException.class)
    public ResponseEntity<ErrorResponseDTO> handleKakaoApiParseException(
        KakaoApiParseException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(),
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(PartyGetCustomException.class)
    public ResponseEntity<ErrorResponseDTO> handlePartyServiceException(PartyGetCustomException ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // 그 외 잡히지 않은 모든 예외에 대한 전역 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception ex,
        HttpServletRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.",
            request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
