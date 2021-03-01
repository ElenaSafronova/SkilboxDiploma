package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.PostComment;
import ru.skillbox.diploma.repository.PostCommentRepository;
import ru.skillbox.diploma.repository.PostRepository;

@Service
public class PostCommentService {
    @Autowired
    private PostCommentRepository postCommentRepository;

    public PostComment findById(int commentId) {
        return postCommentRepository.findById(commentId);
    }

    public void save(PostComment postComment) {
        postCommentRepository.save(postComment);
    }
}
