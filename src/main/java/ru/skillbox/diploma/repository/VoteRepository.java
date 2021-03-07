package ru.skillbox.diploma.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.model.Vote;
import ru.skillbox.diploma.value.PostStatus;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Integer> {
    int countByValue(byte value);

    int countByUserAndValue(User curUser, byte value);

    Vote findByUserAndPost(User curUser, Post curPost);
}
