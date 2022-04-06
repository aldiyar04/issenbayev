package kz.iitu.itse1910.issenbayev.feature.validation;

import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.entity.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UserDtoRoleValidator implements ConstraintValidator<CheckUserDtoRole, String> {

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(role, UserDto.ROLE_DEVELOPER) ||
                Objects.equals(role, UserDto.ROLE_LEAD_DEV) ||
                Objects.equals(role, UserDto.ROLE_MANAGER) ||
                Objects.equals(role, UserDto.ROLE_ADMIN);
    }
}
