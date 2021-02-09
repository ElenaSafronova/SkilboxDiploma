package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.repository.PostRepository;

import java.util.Map;
=======
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

>>>>>>> dev

@Controller
public class DefaultController {
    Logger logger = LoggerFactory.getLogger(DefaultController.class);

<<<<<<< HEAD
    @Autowired
    private PostRepository postRepository;

    @RequestMapping("/")
    public String index(Map<String, Object> model){
        logger.trace("Default method accessed");

        Iterable<Post> posts = postRepository.findAll();
        model.put("posts", posts);
=======
    @RequestMapping("/")
    public String index(Model model){
        logger.trace("Default method accessed");
>>>>>>> dev
        return "index";
    }
}
