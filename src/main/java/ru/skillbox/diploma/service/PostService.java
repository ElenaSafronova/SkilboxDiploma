package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.repository.PostRepository;


@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

}
