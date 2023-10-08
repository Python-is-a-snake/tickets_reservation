package com.trs.tickets.repository;

import com.trs.tickets.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void userSaveShouldWork() {
        user.setUsername("jd@mydomain.comm");
        user.setPassword("jdpassword");

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());
    }

    @Test
    void userSaveWithoutUsernameShouldFail() {
        user.setPassword("jdpassword");

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void userSaveWithoutPasswordShouldFail() {
        user.setUsername("jd@mydomain.comm");

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.save(user));
    }

    @Test
    void userSaveWithNonUniqueEmailShouldFail() {
        User user1 = new User();
        user1.setUsername("jd@mydomain.comm");
        user1.setPassword("jdpassword");

        User user2 = new User();
        user2.setUsername("jd@mydomain.comm");
        user2.setPassword("jdpassword");

        userRepository.saveAndFlush(user1);

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(user2));
    }
}
