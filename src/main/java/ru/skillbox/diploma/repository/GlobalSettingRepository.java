package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.GlobalSetting;


@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {
}
