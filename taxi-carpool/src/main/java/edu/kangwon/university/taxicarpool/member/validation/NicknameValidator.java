package edu.kangwon.university.taxicarpool.member.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<NicknameValid, String> {

    private static final Pattern NICKNAME_PATTERN =
        Pattern.compile("^[가-힣a-zA-Z0-9]{2,20}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null이거나 공백의 경우 해당 메서드에서 처리하지 않음
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return NICKNAME_PATTERN.matcher(value).matches();
    }
}