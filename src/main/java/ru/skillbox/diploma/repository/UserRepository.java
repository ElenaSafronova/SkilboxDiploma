package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.value.PostStatus;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findById(int id);

    User findByName(String name);

    User findByPassword(String password);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}
