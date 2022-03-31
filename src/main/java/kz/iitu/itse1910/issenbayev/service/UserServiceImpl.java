package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserResp;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.feature.mapper.UserMapper;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {RecordAlreadyExistsException.class})
    public UserResp register(UserSignupReq signupReq) {
        throwIfAlreadyTaken(signupReq.getEmail(), signupReq.getUsername());
        User user = toEntity(signupReq);
        user.setRole(User.ROLE_DEVELOPER);
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

        // TODO: check if newEmail and newUsername are already taken and throw exception if that's the case

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

    private void throwIfAlreadyTaken(String email, String username) {
        boolean isUsernameTaken = userRepository.existsByUsername(username);
        boolean isEmailTaken = userRepository.existsByEmail(email);

        if (isUsernameTaken && isEmailTaken) {
            throw new RecordAlreadyExistsException(String.format("Email %s and username %s are already taken",
                    email, username));
        } else if (isUsernameTaken) {
            throw new RecordAlreadyExistsException("Username " + username + " is already taken");
        } else if (isEmailTaken) {
            throw new RecordAlreadyExistsException("Email " + email + " is already taken");
        }
    }

    private User getByIdOrThrowNotFound(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " does not exist"));
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
