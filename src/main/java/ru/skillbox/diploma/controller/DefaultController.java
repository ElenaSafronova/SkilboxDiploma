package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {
    Logger logger = LoggerFactory.getLogger(DefaultController.class);

    @RequestMapping(value = {"/", "/posts/recent"})
    public String index(Model model){
        logger.trace("Default method accessed");
        return "index";
    }

    @RequestMapping(method = {RequestMethod.OPTIONS,
            RequestMethod.GET}, value = "/**/{path:[^\\\\.]*}")
    public String redirectToIndex() {
        return "forward:/";
    }
}
