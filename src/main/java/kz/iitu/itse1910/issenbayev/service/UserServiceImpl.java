package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.response.UserResp;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.helper.exception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.helper.exception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.helper.mapper.UserMapper;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResp register(UserSignupReq signupReq) {
        throwIfAlreadyExists(signupReq);
        User user = toEntity(signupReq);
        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    @Override
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

    @Override
    public UserResp update(UserUpdateReq updateReq) {
        User user = getByIdOrThrowNotFound(updateReq.getId());

        String newRole = updateReq.getNewRole();
        String newEmail = updateReq.getNewEmail();
        String newUsername = updateReq.getNewUsername();

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

    @Override
    public void delete(Long id) {
        User user = getByIdOrThrowNotFound(id);
        userRepository.delete(user);
    }

    private void throwIfAlreadyExists(UserSignupReq signupReq) {
        boolean isUsernameTaken = userRepository.existsByUsername(signupReq.getUsername());
        boolean isEmailTaken = userRepository.existsByEmail(signupReq.getEmail());

        if (isUsernameTaken && isEmailTaken) {
            throw new RecordAlreadyExistsException(String.format("Email %s and username %s are already taken",
                    signupReq.getEmail(), signupReq.getUsername()));
        } else if (isUsernameTaken) {
            throw new RecordAlreadyExistsException("Username " + signupReq.getUsername() + " is already taken");
        } else if (isEmailTaken) {
            throw new RecordAlreadyExistsException("Email " + signupReq.getEmail() + " is already taken");
        }
    }

    private User getByIdOrThrowNotFound(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new RecordNotFoundException("User with id " + id + " does not exist");
                });
    }

    private User toEntity(UserSignupReq signupReq) {
        return UserMapper.INSTANCE.toEntity(signupReq);
    }

    private UserResp toResponse(User user) {
        return UserMapper.INSTANCE.toResponse(user);
    }

    private void throwIfPasswordNotMatches(String actualPasswd, String expectedPasswd) {
        if (!Objects.equals(expectedPasswd, actualPasswd)) {
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
