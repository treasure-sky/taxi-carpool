package edu.kangwon.university.taxicarpool;

import edu.kangwon.university.taxicarpool.email.exception.EmailSendFailedException;
import edu.kangwon.university.taxicarpool.email.exception.EmailVerificationNotFoundException;
import edu.kangwon.university.taxicarpool.email.exception.ExpiredVerificationCodeException;
import edu.kangwon.university.taxicarpool.email.exception.InvalidVerificationCodeException;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedEmailException;
import edu.kangwon.university.taxicarpool.member.exception.DuplicatedNicknameException;
import edu.kangwon.university.taxicarpool.member.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<?> handleMemberNotFoundException(MemberNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedNicknameException.class)
    public ResponseEntity<?> handleDuplicatedNicknameException(DuplicatedNicknameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<?> handleDuplicatedNicknameException(DuplicatedEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(EmailVerificationNotFoundException.class)
    public ResponseEntity<?> handleEmailVerificationNotFoundException(
        EmailVerificationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(e.getMessage());
    }

    @ExceptionHandler(EmailSendFailedException.class)
    public ResponseEntity<?> handleEmailSendFailedException(EmailSendFailedException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }

    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<?> handleInvalidVerificationCodeException(
        InvalidVerificationCodeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(e.getMessage());
    }

    @ExceptionHandler(ExpiredVerificationCodeException.class)
    public ResponseEntity<?> handleExpiredVerificationCodeException(
        ExpiredVerificationCodeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(e.getMessage());
    }


}

