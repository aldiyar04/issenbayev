package kz.iitu.itse1910.issenbayev.service;

import kz.iitu.itse1910.issenbayev.dto.user.request.UserPasswdChangeReq;
import kz.iitu.itse1910.issenbayev.dto.user.request.UserSignupReq;
import kz.iitu.itse1910.issenbayev.dto.user.response.UserResp;
import kz.iitu.itse1910.issenbayev.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegister() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        UserResp result = underTest.register(new UserSignupReq("email", "username", "password"));
        Assertions.assertNull(result);
    }

    @Test
    void testChangePassword() {
        underTest.changePassword(new UserPasswdChangeReq(1L, "oldPassword", "newPassword"));
    }

    @Test
    void testUpdate() {
        UserResp result = underTest.update(null);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testDelete() {
        underTest.delete(1L);
    }
}
