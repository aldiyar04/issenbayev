package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserDto;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import kz.iitu.itse1910.issenbayev.service.specification.UserRoleSpecification;
import kz.iitu.itse1910.issenbayev.service.testdata.UserTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    UserRepository userRepositoryMock;
    @Mock
    UserRoleSpecification roleSpecMock;
    @InjectMocks
    UserService underTest;

    UserTestData.Entity users = new UserTestData.Entity();
    UserTestData.Dto userDtos = new UserTestData.Dto();
    UserTestData.RoleSpecification roleSpec = new UserTestData.RoleSpecification();
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
        when(userRepositoryMock.findAll(pageRequest)).thenReturn(new PageImpl<>(users.getAllUsers()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllUsers());
    }

    @Test
    void getUsers_shouldReturnAdmins_whenRoleAdminAndIsAssignedNull() {
        // given
        String role = UserDto.ROLE_ADMIN;
        Boolean isAssignedToProject = null;
        when(userRepositoryMock.findAll(roleSpec.getForAdmin(), pageRequest))
                .thenReturn(new PageImpl<>(users.getAllAdmins()));
        when(roleSpecMock.getFor(role)).thenReturn(roleSpec.getForAdmin());

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllAdmins());
    }

    @Test
    void getUsers_shouldReturnManagers_whenRoleManagerAndIsAssignedNull() {
        // given
        String role = UserDto.ROLE_MANAGER;
        Boolean isAssignedToProject = null;
        when(userRepositoryMock.findAll(roleSpec.getForManager(), pageRequest))
                .thenReturn(new PageImpl<>(users.getAllManagers()));
        when(roleSpecMock.getFor(role)).thenReturn(roleSpec.getForManager());

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllManagers());
    }

    @Test
    void getUsers_shouldReturnLeadDevs_whenRoleLeadDevAndIsAssignedNull() {
        // given
        String role = UserDto.ROLE_LEAD_DEV;
        Boolean isAssignedToProject = null;
        when(userRepositoryMock.findAll(roleSpec.getForLeadDev(), pageRequest))
                .thenReturn(new PageImpl<>(users.getAllLeadDevs()));
        when(roleSpecMock.getFor(role)).thenReturn(roleSpec.getForLeadDev());

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllLeadDevs());
    }

    @Test
    void getUsers_shouldReturnDevelopers_whenRoleDeveloperAndIsAssignedNull() {
        // given
        String role = UserDto.ROLE_DEVELOPER;
        Boolean isAssignedToProject = null;
        when(userRepositoryMock.findAll(roleSpec.getForDeveloper(), pageRequest))
                .thenReturn(new PageImpl<>(users.getAllDevelopers()));
        when(roleSpecMock.getFor(role)).thenReturn(roleSpec.getForDeveloper());

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAllDevelopers());
    }

    @Test
    void getUsers_shouldReturnUnassignedLeadDevs_whenRoleLeadDevAndIsAssignedFalse() {
        // given
        String role = UserDto.ROLE_LEAD_DEV;
        Boolean isAssignedToProject = false;
        when(userRepositoryMock.findUnassignedLeadDevs(pageRequest))
                .thenReturn(new PageImpl<>(users.getUnassignedLeadDevs()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getUnassignedLeadDevs());
    }

    @Test
    void getUsers_shouldReturnAssignedLeadDevs_whenRoleLeadDevAndIsAssignedTrue() {
        // given
        String role = UserDto.ROLE_LEAD_DEV;
        Boolean isAssignedToProject = true;
        when(userRepositoryMock.findAssignedLeadDevs(pageRequest))
                .thenReturn(new PageImpl<>(users.getAssignedLeadDevs()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAssignedLeadDevs());
    }

    @Test
    void getUsers_shouldReturnUnassignedDevelopers_whenRoleDeveloperAndIsAssignedFalse() {
        // given
        String role = UserDto.ROLE_DEVELOPER;
        Boolean isAssignedToProject = false;
        when(userRepositoryMock.findUnassignedDevelopers(pageRequest))
                .thenReturn(new PageImpl<>(users.getUnassignedDevelopers()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getUnassignedDevelopers());
    }

    @Test
    void getUsers_shouldReturnAssignedDevelopers_whenRoleDeveloperAndIsAssignedTrue() {
        // given
        String role = UserDto.ROLE_DEVELOPER;
        Boolean isAssignedToProject = true;
        when(userRepositoryMock.findAssignedDevelopers(pageRequest))
                .thenReturn(new PageImpl<>(users.getAssignedDevelopers()));

        // when
        List<UserDto> result = underTest.getUsers(pageRequest, role, isAssignedToProject).getUserDtos();

        // then
        assertThat(result).isEqualTo(userDtos.getAssignedDevelopers());
    }

    @Test
    void testGetById() {
        UserDto result = underTest.getById(0L);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testRegister() {
        when(userRepositoryMock.existsByUsername(anyString())).thenReturn(true);
        when(userRepositoryMock.existsByEmail(anyString())).thenReturn(true);

        UserDto result = underTest.register(new UserSignupReq("email", "username", "password"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdate() {
        when(userRepositoryMock.existsByUsername(anyString())).thenReturn(true);
        when(userRepositoryMock.existsByEmail(anyString())).thenReturn(true);

        UserDto result = underTest.update(0L, null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testChangePassword() {
        underTest.changePassword(0L, new UserPasswdChangeReq("oldPassword", "newPassword"));
    }

    @Test
    void testDelete() {
        underTest.delete(Long.valueOf(1));
    }
}