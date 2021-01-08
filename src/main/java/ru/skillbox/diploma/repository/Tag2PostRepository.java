package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.Tag2Post;

@Repository
public interface Tag2PostRepository extends CrudRepository<Tag2Post, Integer> {
}
