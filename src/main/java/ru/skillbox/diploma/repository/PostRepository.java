package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.Post;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
}
