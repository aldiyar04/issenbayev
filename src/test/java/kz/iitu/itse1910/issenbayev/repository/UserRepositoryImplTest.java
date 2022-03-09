package kz.iitu.itse1910.issenbayev.repository;

import kz.iitu.itse1910.issenbayev.IssenbayevApplication;
import kz.iitu.itse1910.issenbayev.config.JpaConfig;
import kz.iitu.itse1910.issenbayev.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IssenbayevApplication.class, JpaConfig.class, UserRepositoryImpl.class})
@TestPropertySource("classpath:application.properties")
class UserRepositoryImplTest {

    UserRepositoryImpl userRepositoryImpl;
    ConfigurableEnvironment env;

    @Autowired
    public UserRepositoryImplTest(UserRepositoryImpl userRepositoryImpl, ConfigurableEnvironment env) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.env = env;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindAllPaginated() {
        // arrange
        int offset = 0;
        int size = 3;

        // act
        List<User> result = userRepositoryImpl.findAllPaginated(offset, size);

        // assert
        int id = 1;
        for (User u: result) {
            Assertions.assertEquals(id++, u.getId());
        }
    }

    @Test
    void testFindById() {
        // arrange
        long id = 1L;
        String username = "H2Admin";

        // act
        Optional<User> result = userRepositoryImpl.findById(id);

        // assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(username, result.get().getUsername());
    }

    @Test
    void testSave() {
        userRepositoryImpl.save(new User("Admin", "email", "username", LocalDate.of(2022, Month.MARCH, 2)));
        User result = userRepositoryImpl.findById(11L).orElse(null);
        Assertions.assertNotNull(result);
    }

    @Test
    void testDelete() {
//        userRepositoryImpl.delete());
    }
}