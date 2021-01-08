package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
}
