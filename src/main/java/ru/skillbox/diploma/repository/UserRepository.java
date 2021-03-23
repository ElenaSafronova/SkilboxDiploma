package ru.skillbox.diploma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findById(int id);

    User findByName(String name);

    User findByCode(String code);

    User findByPassword(String password);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    @Modifying
    @Query("update User u set u.name = :name where u.id = :id")
    void updateName(@Param(value = "id") int id, @Param(value = "name") String name);

    @Modifying
    @Query("update User u set u.email = :email where u.id = :id")
    void updateEmail(@Param(value = "id") int id, @Param(value = "email") String email);

    @Modifying
    @Query("update User u set u.password = :password where u.id = :id")
    void updatePassword(@Param(value = "id") int id, @Param(value = "password") String password);

    @Modifying
    @Query("update User u set u.photo = :photo where u.id = :id")
    void updatePhoto(@Param(value = "id") int id, @Param(value = "photo") String photo);
}
