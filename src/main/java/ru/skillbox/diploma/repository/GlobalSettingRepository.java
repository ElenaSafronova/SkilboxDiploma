package ru.skillbox.diploma.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.GlobalSetting;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {
}
