package edu.kangwon.university.taxicarpool.member.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordValid {

    String message() default "비밀번호는 영문과 숫자를 포함한 8~20자여야 합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}