package ru.skillbox.diploma.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diploma.model.Captcha;

import java.time.LocalDateTime;

@Repository
public interface CaptchaRepository extends CrudRepository<Captcha, Integer> {
    @Transactional
    @Modifying
    @Query("delete from Captcha c where c.time < ?1")
    void deleteBooks(LocalDateTime time);
}
