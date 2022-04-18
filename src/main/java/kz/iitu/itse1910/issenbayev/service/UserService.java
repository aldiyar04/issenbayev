package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.controller.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.controller.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.controller.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.controller.dto.user.response.UserPaginatedResp;
import kz.iitu.itse1910.issenbayev.repository.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.ApiExceptionDetailHolder;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.service.mapper.UserMapper;
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
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleSpecification roleSpec;

    @Transactional(readOnly = true)
    public UserPaginatedResp getUsers(Pageable pageable,
                                      Optional<UserDto.Role> roleOptional,
                                      Optional<Boolean> isAssignedToProjectOptional) {
        Page<User> resultUserPage = null;

        if (roleOptional.isEmpty() && isAssignedToProjectOptional.isEmpty()) {
            resultUserPage = userRepository.findAll(pageable);
        } else if (roleOptional.isPresent()) {
            UserDto.Role role = roleOptional.get();
            if (isAssignedToProjectOptional.isEmpty()) {
                resultUserPage = userRepository.findAll(roleSpec.getForUserDtoRole(role), pageable);
            } else {
                boolean isAssignedToProject = isAssignedToProjectOptional.get();
                if (role.equals(UserDto.Role.LEAD_DEV) && !isAssignedToProject) {
                    resultUserPage = userRepository.findUnassignedLeadDevs(pageable);
                } else if (role.equals(UserDto.Role.LEAD_DEV) && isAssignedToProject) {
                    resultUserPage = userRepository.findAssignedLeadDevs(pageable);
                } else if (role.equals(UserDto.Role.DEVELOPER) && !isAssignedToProject) {
                    resultUserPage = userRepository.findUnassignedDevelopers(pageable);
                } else if (role.equals(UserDto.Role.DEVELOPER) && isAssignedToProject) {
                    resultUserPage = userRepository.findAssignedDevelopers(pageable);
                }
            }
        }
        return UserPaginatedResp.fromUserPage(resultUserPage);
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

        User.Role newRole = toEntityRole(updateReq.getRole());
        String newEmail = updateReq.getEmail();
        String newUsername = updateReq.getUsername();

        throwIfAlreadyTaken(newEmail, newUsername);

        if (newRole != null) {
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

    // TODO: uncomment when password hashing is implemented
//    public void changePassword(long id, UserPasswdChangeReq passChangeReq) {
//        User user = getByIdOrThrowNotFound(id);
//
//        String supplied = passChangeReq.getOldPassword();
//        String expected = user.getPassword();
//        throwIfPasswordNotMatches(supplied, expected);
//
//        String newPassword = passChangeReq.getNewPassword();
//        user.setPassword(newPassword);
//        userRepository.save(user);
//    }

    public void delete(Long id) {
        User user = getByIdOrThrowNotFound(id);
        userRepository.delete(user);
    }

    private User toEntity(UserSignupReq signupReq) {
        return UserMapper.INSTANCE.signupReqToEntity(signupReq);
    }

    private UserDto toDto(User user) {
        return UserMapper.INSTANCE.toDto(user);
    }

    private User.Role toEntityRole(UserDto.Role dtoRole) {
        return UserMapper.INSTANCE.toEntityRole(dtoRole);
    }

    private void throwIfAlreadyTaken(String email, String username) {
        boolean isUsernameTaken = userRepository.existsByUsername(username);
        boolean isEmailTaken = userRepository.existsByEmail(email);

        List<ApiExceptionDetailHolder> exDetailHolders = new ArrayList<>();
        if (isEmailTaken) {
            exDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserDto.Field.EMAIL)
                    .message("Email '" + email + "' is already taken")
                    .build());
        }
        if (isUsernameTaken) {
            exDetailHolders.add(ApiExceptionDetailHolder.builder()
                    .field(UserDto.Field.USERNAME)
                    .message("Username '" + username + "' is already taken")
                    .build());
        }
        if (!exDetailHolders.isEmpty()) {
            throw new RecordAlreadyExistsException(exDetailHolders);
        }
    }

    private User getByIdOrThrowNotFound(long id) {
        ApiExceptionDetailHolder exDetailHolder = ApiExceptionDetailHolder.builder()
                .field(UserDto.Field.ID)
                .message("User with id " + id + " does not exist")
                .build();
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(exDetailHolder));
    }

    // TODO: uncomment and change impl when password hashing is implemented
//    private void throwIfPasswordNotMatches(String actualPassword, String expectedPassword) {
//        if (!Objects.equals(expectedPassword, actualPassword)) {
//            throw new IncorrectPasswordException();
//        }
//    }
}
