package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.repository.PostRepository;

import java.util.Map;

@Controller
public class DefaultController {
    Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @Autowired
    private PostRepository postRepository;

    @RequestMapping("/")
    public String index(Map<String, Object> model){
        logger.trace("Default method accessed");

        Iterable<Post> posts = postRepository.findAll();
        model.put("posts", posts);
        return "index";
    }
}
