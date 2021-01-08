package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.CaptchaCode;

@Repository
public interface CaptchaCodeRepository extends CrudRepository<CaptchaCode, Integer> {
}
