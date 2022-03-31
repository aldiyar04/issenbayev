package kz.iitu.itse1910.issenbayev.feature.validation;

import kz.iitu.itse1910.issenbayev.entity.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UserRoleValidator implements ConstraintValidator<CheckUserRole, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(role, User.ROLE_DEVELOPER) ||
                Objects.equals(role, User.ROLE_LEAD_DEV) ||
                Objects.equals(role, User.ROLE_MANAGER) ||
                Objects.equals(role, User.ROLE_ADMIN);
    }
}
