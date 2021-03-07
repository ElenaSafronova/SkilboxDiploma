package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.Tag2Post;
import ru.skillbox.diploma.repository.Tag2PostRepository;
import ru.skillbox.diploma.repository.TagRepository;

@Service
public class Tag2PostService {
    @Autowired
    private Tag2PostRepository tag2PostRepository;

    public void save(Tag2Post tag2Post) {
        tag2PostRepository.save(tag2Post);
    }
}
