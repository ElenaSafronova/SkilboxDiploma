package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.GlobalSetting;
import ru.skillbox.diploma.model.PostComment;

@Repository
public interface PostCommentRepository extends CrudRepository<PostComment, Integer> {
    PostComment findById(int id);
}
