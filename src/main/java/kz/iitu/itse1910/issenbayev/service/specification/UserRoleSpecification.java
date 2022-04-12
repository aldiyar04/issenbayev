package kz.iitu.itse1910.issenbayev.service.specification;

import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.entity.User_;
import kz.iitu.itse1910.issenbayev.feature.mapper.UserMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserRoleSpecification {
    public Specification<User> getForUserDtoRole(UserDto.Role userDtoRole) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User_.ROLE),
                toUserEntityRole(userDtoRole));
    }

    private User.Role toUserEntityRole(UserDto.Role userDtoRole) {
        return UserMapper.INSTANCE.toEntityRole(userDtoRole);
    }
}
