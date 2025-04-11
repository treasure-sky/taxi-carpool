package edu.kangwon.university.taxicarpool.member.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class NicknameValidator implements ConstraintValidator<NicknameValid, String> {

    private static final Pattern NICKNAME_PATTERN =
        Pattern.compile("^[가-힣a-zA-Z0-9]{2,20}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && NICKNAME_PATTERN.matcher(value).matches();
    }
}