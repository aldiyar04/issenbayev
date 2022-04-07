package kz.iitu.itse1910.issenbayev.service.specification;

import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.entity.User_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserRoleSpecification {
    public Specification<User> getFor(String role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.ROLE),
                toUserEntityRole(role));
    }

    private String toUserEntityRole(String userDtoRole) {
        if (userDtoRole.equals(UserDto.ROLE_ADMIN)) {
            return User.Role.ADMIN;
        } else if (userDtoRole.equals(UserDto.ROLE_MANAGER)) {
            return User.Role.MANAGER;
        } else if (userDtoRole.equals(UserDto.ROLE_LEAD_DEV)) {
            return User.Role.LEAD_DEV;
        } else if (userDtoRole.equals(UserDto.ROLE_DEVELOPER)) {
            return User.Role.DEVELOPER;
        }
        throw new IllegalStateException("Invalid user role passed. Role must be validated before reaching this method.");
    }
}
