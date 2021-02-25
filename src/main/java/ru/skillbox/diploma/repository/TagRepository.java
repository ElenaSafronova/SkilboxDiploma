package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.Tag;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    Iterable<Tag> findByNameContaining(String tagQuery);

    Tag findByName(String name);
}
