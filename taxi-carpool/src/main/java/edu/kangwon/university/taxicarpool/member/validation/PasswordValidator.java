package edu.kangwon.university.taxicarpool.member.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {

    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && PASSWORD_PATTERN.matcher(value).matches();
    }
}