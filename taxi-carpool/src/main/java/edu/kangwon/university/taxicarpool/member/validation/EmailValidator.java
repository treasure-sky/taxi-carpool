package edu.kangwon.university.taxicarpool.member.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailValid, String> {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9._%+-]+@kangwon\\.ac\\.kr$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null이거나 공백의 경우 해당 메서드에서 처리하지 않음
        if (value == null || value.trim().isEmpty()) {
            return true;
        }
        return EMAIL_PATTERN.matcher(value).matches();
    }
}