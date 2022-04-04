package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserResp;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.ApiExceptionDetailHolder;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.UserMapper;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResp register(UserSignupReq signupReq) {
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



    public UserResp update(UserUpdateReq updateReq) {
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

    private void throwIfAlreadyTaken(String email, String username) {
        boolean isUsernameTaken = userRepository.existsByUsername(username);
        boolean isEmailTaken = userRepository.existsByEmail(email);

        List<ApiExceptionDetailHolder> exceptionDetailHolders = new ArrayList<>();
        if (isEmailTaken) {
            exceptionDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserResp.FIELD_EMAIL)
                    .message("Email \"" + email + "\" is already taken.")
                    .build());
        }
        if (isUsernameTaken) {
            exceptionDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserResp.FIELD_USERNAME)
                    .message("Username \"" + username + "\" is already taken.")
                    .build());
        }
        throw new RecordAlreadyExistsException(exceptionDetailHolders);
    }

    private User getByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exceptionDetailHolder = ApiExceptionDetailHolder.builder()
                .field(UserResp.FIELD_ID)
                .message("User with id " + id + " does not exist.")
                .build();
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(exceptionDetailHolder));
    }

    private User toEntity(UserSignupReq signupReq) {
        return UserMapper.INSTANCE.toEntity(signupReq);
    }

    private UserResp toResponse(User user) {
        return UserMapper.INSTANCE.toResponse(user);
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
