package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserPaginatedResp;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.*;
import kz.iitu.itse1910.issenbayev.feature.mapper.UserMapper;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import kz.iitu.itse1910.issenbayev.service.specification.UserRoleSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleSpecification roleSpec;

    @Transactional(readOnly = true)
    public UserPaginatedResp getUsers(Pageable pageable, String role, Boolean isAssignedToProject) {
        Page<User> resultUserPage = null;

        if (role == null && isAssignedToProject == null) {
            resultUserPage = userRepository.findAll(pageable);
        } else if (isAssignedToProject != null &&
                (role == null || !(role.equals(UserDto.ROLE_LEAD_DEV) || role.equals(UserDto.ROLE_DEVELOPER)))) {
            throwApiExInvalidCombinationOfParams();
        } else if (role != null) {
            if (isAssignedToProject == null) {
                resultUserPage = userRepository.findAll(roleSpec.getFor(role), pageable);
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

    private void throwApiExInvalidCombinationOfParams() {
        String exMsg = String.format("%s can be used only if %s is specified as '%s' or '%s'",
                UserDto.FILTER_IS_ASSIGNED_TO_PROJECT, UserDto.FIELD_ROLE,
                UserDto.ROLE_LEAD_DEV, UserDto.ROLE_DEVELOPER);
        throw new ApiException(exMsg);
    }

    public UserDto getById(long id) {
        User user = getByIdOrThrowNotFound(id);
        return toDto(user);
    }

    public UserDto register(UserSignupReq signupReq) {
        throwIfAlreadyTaken(signupReq.getEmail(), signupReq.getUsername());
        User user = toEntity(signupReq);
        user.setRole(User.Role.DEVELOPER);
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    public UserDto update(long id, UserUpdateReq updateReq) {
        User user = getByIdOrThrowNotFound(id);

        String newRole = updateReq.getRole();
        String newEmail = updateReq.getEmail();
        String newUsername = updateReq.getUsername();

        throwIfAlreadyTaken(newEmail, newUsername);

        if (StringUtils.hasText(newRole)) {
            user.setRole(newRole);
        }
        if (StringUtils.hasText(newEmail)) {
            user.setEmail(newEmail);
        }
        if (StringUtils.hasText(newUsername)) {
            user.setUsername(newUsername);
        }

        User updatedUser = userRepository.save(user);
        return toDto(updatedUser);
    }

    public void changePassword(long id, UserPasswdChangeReq passChangeReq) {
        User user = getByIdOrThrowNotFound(id);

        String supplied = passChangeReq.getOldPassword();
        String expected = user.getPassword();
        throwIfPasswordNotMatches(supplied, expected);

        String newPassword = passChangeReq.getNewPassword();
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void delete(Long id) {
        User user = getByIdOrThrowNotFound(id);
        userRepository.delete(user);
    }

    private User toEntity(UserSignupReq signupReq) {
        return UserMapper.INSTANCE.toEntity(signupReq);
    }

    private UserDto toDto(User user) {
        return UserMapper.INSTANCE.toDto(user);
    }

    private void throwIfAlreadyTaken(String email, String username) {
        boolean isUsernameTaken = userRepository.existsByUsername(username);
        boolean isEmailTaken = userRepository.existsByEmail(email);

        List<ApiExceptionDetailHolder> exDetailHolders = new ArrayList<>();
        if (isEmailTaken) {
            exDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserDto.FIELD_EMAIL)
                    .message("Email '" + email + "' is already taken")
                    .build());
        }
        if (isUsernameTaken) {
            exDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserDto.FIELD_USERNAME)
                    .message("Username '" + username + "' is already taken")
                    .build());
        }
        if (!exDetailHolders.isEmpty()) {
            throw new RecordAlreadyExistsException(exDetailHolders);
        }
    }

    private User getByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
                .field(UserDto.FIELD_ID)
                .message("User with id " + id + " does not exist")
                .build();
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(exDetailHolder));
    }

    private void throwIfPasswordNotMatches(String actualPassword, String expectedPassword) {
        if (!Objects.equals(expectedPassword, actualPassword)) {
            throw new IncorrectPasswordException();
        }
    }
}
