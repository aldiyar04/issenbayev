package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserPaginatedResp;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.ApiException;
import kz.iitu.itse1910.issenbayev.feature.exception.ApiExceptionDetailHolder;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.UserMapper;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserPaginatedResp getUsers(Pageable pageable, String role, Boolean isAssignedToProject) {
        Page<User> resultUserPage = null;

        if (role == null && isAssignedToProject == null) {
            resultUserPage = userRepository.findAll(pageable);
        } else if (isAssignedToProject != null &&
                (role == null || !(role.equals(UserDto.ROLE_LEAD_DEV) || role.equals(UserDto.ROLE_DEVELOPER)))) {
            String exMsg = String.format("%s can be used only if %s is specified as \"%s\" or \"%s\"",
                    UserDto.FILTER_IS_ASSIGNED_TO_PROJECT, UserDto.FIELD_ROLE,
                    UserDto.ROLE_LEAD_DEV, UserDto.ROLE_DEVELOPER);
            ApiExceptionDetailHolder detailHolder = ApiExceptionDetailHolder.builder()
                    .message(exMsg)
                    .build();
            throw new ApiException(detailHolder);
        } else if (role != null) {
            if (isAssignedToProject == null) {
                resultUserPage = userRepository.findAll(getRoleSpecification(role), pageable);
            } else {
                if (role.equals(UserDto.ROLE_LEAD_DEV)) {
                    if (!isAssignedToProject) {
                        resultUserPage = userRepository.findUnassignedLeadDevs(pageable);
                    } else {
                        resultUserPage = userRepository.findAssignedLeadDevs(pageable);
                    }
                } else if (role.equals(UserDto.ROLE_DEVELOPER)) {
                    if (!isAssignedToProject) {
                        resultUserPage = userRepository.findUnassignedDevelopers(pageable);
                    } else {
                        resultUserPage = userRepository.findAssignedDevelopers(pageable);
                    }
                }
            }
        }
        return UserPaginatedResp.fromUserPage(resultUserPage);
    }

    private Specification<User> getRoleSpecification(String role) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User.COLUMN_ROLE),
                toUserEntityRole(role));
    }

    private String toUserEntityRole(String userDtoRole) {
        if (userDtoRole.equals(UserDto.ROLE_ADMIN)) {
            return User.ROLE_ADMIN;
        } else if (userDtoRole.equals(UserDto.ROLE_MANAGER)) {
            return User.ROLE_MANAGER;
        } else if (userDtoRole.equals(UserDto.ROLE_LEAD_DEV)) {
            return User.ROLE_LEAD_DEV;
        } else if (userDtoRole.equals(UserDto.ROLE_DEVELOPER)) {
            return User.ROLE_DEVELOPER;
        }
        throw new IllegalStateException("This line cannot be reached since: 1) all the possible valid user roles are " +
                "checked in this method; 2) user role is validated in User Controller.");
    }

    public UserDto register(UserSignupReq signupReq) {
        throwIfAlreadyTaken(signupReq.getEmail(), signupReq.getUsername());
        User user = toEntity(signupReq);
        user.setRole(User.ROLE_DEVELOPER);
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    public void changePassword(UserPasswdChangeReq passChangeReq) {
        long id = passChangeReq.getId();
        User user = getByIdOrThrowNotFound(id);

        String supplied = passChangeReq.getOldPassword();
        String expected = user.getPassword();
        throwIfPasswordNotMatches(supplied, expected);

        String newPassword = passChangeReq.getNewPassword();
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public UserDto update(UserUpdateReq updateReq) {
        User user = getByIdOrThrowNotFound(updateReq.getId());

        String newRole = updateReq.getNewRole();
        String newEmail = updateReq.getNewEmail();
        String newUsername = updateReq.getNewUsername();

        throwIfAlreadyTaken(newEmail, newUsername);

        if (StringUtils.hasText(newRole)) {
            throwIfRoleInvalid(newRole);
            user.setRole(newRole);
        }
        if (StringUtils.hasText(newEmail)) {
            user.setEmail(newEmail);
        }
        if (StringUtils.hasText(newUsername)) {
            user.setUsername(newUsername);
        }

        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }

    public void delete(Long id) {
        User user = getByIdOrThrowNotFound(id);
        userRepository.delete(user);
    }

    private User toEntity(UserSignupReq signupReq) {
        return UserMapper.INSTANCE.toEntity(signupReq);
    }

    private UserDto toResponse(User user) {
        return UserMapper.INSTANCE.toDto(user);
    }

    private void throwIfAlreadyTaken(String email, String username) {
        boolean isUsernameTaken = userRepository.existsByUsername(username);
        boolean isEmailTaken = userRepository.existsByEmail(email);

        List<ApiExceptionDetailHolder> exceptionDetailHolders = new ArrayList<>();
        if (isEmailTaken) {
            exceptionDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserDto.FIELD_EMAIL)
                    .message("Email \"" + email + "\" is already taken.")
                    .build());
        }
        if (isUsernameTaken) {
            exceptionDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserDto.FIELD_USERNAME)
                    .message("Username \"" + username + "\" is already taken.")
                    .build());
        }
        throw new RecordAlreadyExistsException(exceptionDetailHolders);
    }

    private User getByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exceptionDetailHolder = ApiExceptionDetailHolder.builder()
                .field(UserDto.FIELD_ID)
                .message("User with id " + id + " does not exist.")
                .build();
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(exceptionDetailHolder));
    }

    private void throwIfPasswordNotMatches(String actualPassword, String expectedPassword) {
        if (!Objects.equals(expectedPassword, actualPassword)) {
            throw new IllegalArgumentException("Password incorrect");
        }
    }

    private void throwIfRoleInvalid(String role) {
        if (!role.equals(User.ROLE_ADMIN) && !role.equals(User.ROLE_MANAGER) &&
                !role.equals(User.ROLE_LEAD_DEV) && !role.equals(User.ROLE_DEVELOPER)) {
            throw new IllegalArgumentException("Role invalid");
        }
    }
}
