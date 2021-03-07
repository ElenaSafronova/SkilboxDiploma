package ru.skillbox.diploma;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.repository.UserRepository;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        User user = new User();
        user.setEmail("blablabla@mail.ru");
        user.setIsModerator((byte) 0);
        user.setName("blabla");
        user.setRegTime(ZonedDateTime.now());
        user.setPassword("123456");

        User newUser = userRepository.save(user);

        User existsUser = entityManager.find(User.class, newUser.getId());

        assertThat(existsUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindByEmail(){
        String email = "Bla@mail.ru";
        User curUser = userRepository.findByEmail(email);
        assertThat(curUser).isNotNull();

    }
}
