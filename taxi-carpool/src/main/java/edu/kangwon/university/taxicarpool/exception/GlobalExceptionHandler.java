package edu.kangwon.university.taxicarpool.exception;

import edu.kangwon.university.taxicarpool.auth.authException.AuthenticationFailedException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenExpiredException;
import edu.kangwon.university.taxicarpool.auth.authException.TokenInvalidException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.MemberNotInPartyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyAlreadyDeletedException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyEmptyException;
import edu.kangwon.university.taxicarpool.party.partyException.PartyNotFoundException;
import edu.kangwon.university.taxicarpool.party.partyException.UnauthorizedHostAccessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//TokenExpiredException는 Jwt에서만 쓰여서 따로 정의 안 해놓음
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PartyNotFoundException.class)
    public ErrorResponse handlePartyNotFoundException(PartyNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ErrorResponse handleAuthenticationFailedException(AuthenticationFailedException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(TokenInvalidException.class)
    public ErrorResponse handleTokenInvalidException(TokenInvalidException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ErrorResponse handleMemberNotFoundException(MemberNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MemberNotInPartyException.class)
    public ErrorResponse handleMemberNotInPartyException(MemberNotInPartyException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PartyAlreadyDeletedException.class)
    public ErrorResponse handlePartyAlreadyDeletedException(PartyAlreadyDeletedException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.GONE.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(PartyEmptyException.class)
    public ErrorResponse handlePartyEmptyException(PartyEmptyException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.GONE.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(UnauthorizedHostAccessException.class)
    public ErrorResponse handleUnauthorizedHostAccessException(UnauthorizedHostAccessException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ErrorResponse handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request.getRequestURI());
    }

    // 그 외 잡히지 않은 모든 예외에 대한 전역 핸들러
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 오류가 발생했습니다.", request.getRequestURI());
    }
}
