package kz.iitu.itse1910.issenbayev.feature.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {UserRoleValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserRole {
    String message() default "User role is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
