package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserUpdateReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.entity.User;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.ApiException;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.RecordAlreadyExistsException;
import kz.iitu.itse1910.issenbayev.feature.exception.apiexception.RecordNotFoundException;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import kz.iitu.itse1910.issenbayev.service.specification.UserRoleSpecification;
import kz.iitu.itse1910.issenbayev.service.testdata.UserTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    UserRoleSpecification roleSpec;
    @InjectMocks
    UserService underTest;

    UserTestData.Entity users = new UserTestData.Entity();
    UserTestData.Dto userDtos = new UserTestData.Dto();
    UserTestData.RoleSpecification roleSpecTestUtil = new UserTestData.RoleSpecification();
    PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUsers_shouldReturnAllUsers_whenRoleAndIsAssignedNull() {
        // given
        String role = null;
        Boolean isAssignedToProject = null;
        when(userRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(users.getAllUsers()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest,
                Optional.ofNullable(role), Optional.ofNullable(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllUsers());
    }

    @Test
    void getUsers_shouldReturnAdmins_whenRoleAdminAndIsAssignedNull() {
        // given
        String adminRole = UserDto.Role.ADMIN;
        Boolean isAssignedToProject = null;
        Specification<User> adminRoleSpec = roleSpecTestUtil.getForAdmin();
        when(userRepository.findAll(adminRoleSpec, pageRequest))
                .thenReturn(new PageImpl<>(users.getAllAdmins()));
        when(roleSpec.getFor(adminRole)).thenReturn(adminRoleSpec);

        // when
        List<UserDto> result = underTest.getUsers(pageRequest,
                        Optional.of(adminRole), Optional.ofNullable(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllAdmins());
    }

    @Test
    void getUsers_shouldReturnManagers_whenRoleManagerAndIsAssignedNull() {
        // given
        String managerRole = UserDto.Role.MANAGER;
        Boolean isAssignedToProject = null;
        Specification<User> managerRoleSpec = roleSpecTestUtil.getForManager();
        when(userRepository.findAll(managerRoleSpec, pageRequest))
                .thenReturn(new PageImpl<>(users.getAllManagers()));
        when(roleSpec.getFor(managerRole)).thenReturn(managerRoleSpec);

        // when
        List<UserDto> result = underTest.getUsers(pageRequest,
                        Optional.of(managerRole), Optional.ofNullable(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllManagers());
    }

    @Test
    void getUsers_shouldReturnLeadDevs_whenRoleLeadDevAndIsAssignedNull() {
        // given
        String leadDevRole = UserDto.Role.LEAD_DEV;
        Boolean isAssignedToProject = null;
        Specification<User> leadDevRoleSpec = roleSpecTestUtil.getForLeadDev();
        when(userRepository.findAll(leadDevRoleSpec, pageRequest))
                .thenReturn(new PageImpl<>(users.getAllLeadDevs()));
        when(roleSpec.getFor(leadDevRole)).thenReturn(leadDevRoleSpec);

        // when
        List<UserDto> result = underTest.getUsers(pageRequest,
                        Optional.of(leadDevRole), Optional.ofNullable(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllLeadDevs());
    }

    @Test
    void getUsers_shouldReturnDevelopers_whenRoleDeveloperAndIsAssignedNull() {
        // given
        String developerRole = UserDto.Role.DEVELOPER;
        Boolean isAssignedToProject = null;
        Specification<User> developerRoleSpec = roleSpecTestUtil.getForDeveloper();
        when(userRepository.findAll(developerRoleSpec, pageRequest))
                .thenReturn(new PageImpl<>(users.getAllDevelopers()));
        when(roleSpec.getFor(developerRole)).thenReturn(developerRoleSpec);

        // when
        List<UserDto> result = underTest.getUsers(pageRequest,
                        Optional.of(developerRole), Optional.ofNullable(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllDevelopers());
    }

    @Test
    void getUsers_shouldReturnUnassignedLeadDevs_whenRoleLeadDevAndIsAssignedFalse() {
        // given
        String role = UserDto.Role.LEAD_DEV;
        Boolean isAssignedToProject = false;
        when(userRepository.findUnassignedLeadDevs(pageRequest))
                .thenReturn(new PageImpl<>(users.getUnassignedLeadDevs()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, Optional.of(role), Optional.of(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getUnassignedLeadDevs());
    }

    @Test
    void getUsers_shouldReturnAssignedLeadDevs_whenRoleLeadDevAndIsAssignedTrue() {
        // given
        String role = UserDto.Role.LEAD_DEV;
        Boolean isAssignedToProject = true;
        when(userRepository.findAssignedLeadDevs(pageRequest))
                .thenReturn(new PageImpl<>(users.getAssignedLeadDevs()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, Optional.of(role), Optional.of(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAssignedLeadDevs());
    }

    @Test
    void getUsers_shouldReturnUnassignedDevelopers_whenRoleDeveloperAndIsAssignedFalse() {
        // given
        String role = UserDto.Role.DEVELOPER;
        Boolean isAssignedToProject = false;
        when(userRepository.findUnassignedDevelopers(pageRequest))
                .thenReturn(new PageImpl<>(users.getUnassignedDevelopers()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, Optional.of(role), Optional.of(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getUnassignedDevelopers());
    }

    @Test
    void getUsers_shouldReturnAssignedDevelopers_whenRoleDeveloperAndIsAssignedTrue() {
        // given
        String role = UserDto.Role.DEVELOPER;
        Boolean isAssignedToProject = true;
        when(userRepository.findAssignedDevelopers(pageRequest))
                .thenReturn(new PageImpl<>(users.getAssignedDevelopers()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, Optional.of(role), Optional.of(isAssignedToProject))
                .getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAssignedDevelopers());
    }

    @Test
    void getUsers_shouldThrowApiException_whenRoleNotLeadDevOrDeveloperAndIsAssignedNotNull() {
        // given
        String nullRole = null;
        String adminRole = User.Role.ADMIN;
        String managerRole = User.Role.MANAGER;
        Boolean isAssignedToProject = true;

        // when, then
        assertThatThrownBy(() -> underTest.getUsers(pageRequest,
                Optional.ofNullable(nullRole), Optional.of(isAssignedToProject)))
                .isInstanceOf(ApiException.class);
        assertThatThrownBy(() -> underTest.getUsers(pageRequest,
                Optional.of(adminRole), Optional.of(isAssignedToProject)))
                .isInstanceOf(ApiException.class);
        assertThatThrownBy(() -> underTest.getUsers(pageRequest,
                Optional.of(managerRole), Optional.of(isAssignedToProject)))
                .isInstanceOf(ApiException.class);
    }

    @Test
    void testGetById_caseSuccess() {
        // given
        long id = 1L;
        User user = User.builder()
                .id(id)
                .build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        UserDto result = underTest.getById(id);

        // then
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void testGetById_caseNotFound() {
        // given
        long id = -1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> underTest.getById(id))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testRegister_caseSuccess() {
        // given
        String email = "email@test.com";
        String username = "username";
        String defaultRole = User.Role.DEVELOPER;
        User user = User.builder()
                .role(defaultRole)
                .email(email)
                .username(username)
                .build();
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // when
        UserSignupReq signupReq = new UserSignupReq(email, username, "password");
        UserDto result = underTest.register(signupReq);

        // then
        ArgumentCaptor<User> userArgCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgCaptor.capture());
        User capturedUser = userArgCaptor.getValue();
        assertThat(capturedUser.getRole()).isEqualTo(defaultRole);
        assertThat(capturedUser.getEmail()).isEqualTo(email);
        assertThat(capturedUser.getUsername()).isEqualTo(username);

        assertThat(result.getRole()).isEqualTo(defaultRole);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    void register_shouldThrowRecordAlreadyExistsException_whenEmailAlreadyExists() {
        // given
        String alreadyExistingEmail = "i_am_taken@test.com";
        String username = "username";
        when(userRepository.existsByEmail(alreadyExistingEmail)).thenReturn(true);
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // when, then
        UserSignupReq signupReq = new UserSignupReq(alreadyExistingEmail, username, "password");
        assertThatThrownBy(() -> underTest.register(signupReq))
                .isInstanceOf(RecordAlreadyExistsException.class);
    }

    @Test
    void register_shouldThrowRecordAlreadyExistsException_whenUsernameAlreadyExists() {
        // given
        String email = "email@test.com";
        String alreadyExistingUsername = "taken_username";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.existsByUsername(alreadyExistingUsername)).thenReturn(true);

        // when, then
        UserSignupReq signupReq = new UserSignupReq(email, alreadyExistingUsername, "password");
        assertThatThrownBy(() -> underTest.register(signupReq))
                .isInstanceOf(RecordAlreadyExistsException.class);
    }

    @Test
    void register_shouldThrowRecordAlreadyExistsException_whenEmailAndUsernameAlreadyExist() {
        // given
        String alreadyExistingEmail = "i_am_taken@test.com";
        String alreadyExistingUsername = "taken_username";
        when(userRepository.existsByEmail(alreadyExistingEmail)).thenReturn(true);
        when(userRepository.existsByUsername(alreadyExistingUsername)).thenReturn(true);

        // when, then
        UserSignupReq signupReq = new UserSignupReq(alreadyExistingEmail, alreadyExistingUsername, "password");
        assertThatThrownBy(() -> underTest.register(signupReq))
                .isInstanceOf(RecordAlreadyExistsException.class);
    }

    @Test
    void testUpdate_caseSuccess() {
        // given
        long id = 1L;
        String newRole = User.Role.LEAD_DEV;
        String newEmail = "new_email@test.com";
        String newUsername = "new_username";
        User updatedUser = User.builder()
                .id(id)
                .role(newRole)
                .email(newEmail)
                .username(newUsername)
                .build();
        when(userRepository.save(any())).thenReturn(updatedUser);
        User userOldVersion = User.builder().id(id).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(userOldVersion));

        // when
        UserUpdateReq updateReq = UserUpdateReq.builder()
                .role(newRole)
                .email(newEmail)
                .username(newUsername)
                .build();
        UserDto result = underTest.update(id, updateReq);

        // then
        ArgumentCaptor<User> userArgCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgCaptor.capture());
        User capturedUser = userArgCaptor.getValue();
        assertThat(capturedUser.getId()).isEqualTo(id);
        assertThat(capturedUser.getRole()).isEqualTo(newRole);
        assertThat(capturedUser.getEmail()).isEqualTo(newEmail);
        assertThat(capturedUser.getUsername()).isEqualTo(newUsername);

        assertThat(result.getId()).isEqualTo(updatedUser.getId());
        assertThat(result.getRole()).isEqualTo(updatedUser.getRole());
        assertThat(result.getEmail()).isEqualTo(updatedUser.getEmail());
        assertThat(result.getUsername()).isEqualTo(updatedUser.getUsername());
    }

    @Test
    void testUpdate_caseNotFound() {
        // given
        long id = -1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> underTest.update(id, any()))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void update_shouldThrowRecordAlreadyExistsException_whenNewEmailAlreadyExists() {
        // given
        long id = 1L;
        String newEmail = "i_am_taken@test.com";
        String newUsername = "new_username";
        User anyUser = User.builder().build();
        when(userRepository.findById(id)).thenReturn(Optional.of(anyUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);
        when(userRepository.existsByUsername(newUsername)).thenReturn(false);

        // when, then
        UserUpdateReq updateReq = UserUpdateReq.builder()
                .email(newEmail)
                .username(newUsername)
                .build();
        assertThatThrownBy(() -> underTest.update(id, updateReq))
               .isInstanceOf(RecordAlreadyExistsException.class);
    }

    @Test
    void update_shouldThrowRecordAlreadyExistsException_whenNewUsernameAlreadyExists() {
        // given
        long id = 1L;
        String newEmail = "new_email@test.com";
        String newUsername = "taken_username";
        User anyUser = User.builder().build();
        when(userRepository.findById(id)).thenReturn(Optional.of(anyUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.existsByUsername(newUsername)).thenReturn(true);

        // when, then
        UserUpdateReq updateReq = UserUpdateReq.builder()
                .email(newEmail)
                .username(newUsername)
                .build();
        assertThatThrownBy(() -> underTest.update(id, updateReq))
                .isInstanceOf(RecordAlreadyExistsException.class);
    }

    @Test
    void update_shouldThrowRecordAlreadyExistsException_whenNewEmailAndNewUsernameAlreadyExist() {
        // given
        long id = 1L;
        String newEmail = "i_am_taken@test.com";
        String newUsername = "taken_username";
        User anyUser = User.builder().build();
        when(userRepository.findById(id)).thenReturn(Optional.of(anyUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);
        when(userRepository.existsByUsername(newUsername)).thenReturn(true);

        // when, then
        UserUpdateReq updateReq = UserUpdateReq.builder()
                .email(newEmail)
                .username(newUsername)
                .build();
        assertThatThrownBy(() -> underTest.update(id, updateReq))
                .isInstanceOf(RecordAlreadyExistsException.class);
    }

    @Test
    void testDelete_caseSuccess() {
        // given
        long id = 1L;
        User user = User.builder().id(id).build();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // when
        underTest.delete(id);

        // then
        ArgumentCaptor<User> userArgCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).delete(userArgCaptor.capture());
        User deletedUser = userArgCaptor.getValue();
        assertThat(deletedUser).isEqualTo(user);
    }

    @Test
    void testDelete_caseNotFound() {
        // given
        long id = -1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> underTest.delete(id))
                .isInstanceOf(RecordNotFoundException.class);
    }
}