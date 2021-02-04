package ru.skillbox.diploma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.value.PostStatus;

import java.time.ZonedDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    static void save(byte b, String name, String e_mail, String password) {
    }

    User findById(int id);

    User findByName(String name);

    User findByPassword(String password);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}
