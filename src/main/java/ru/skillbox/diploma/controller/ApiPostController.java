package ru.skillbox.diploma.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class ApiPostController {
    @RequestMapping("/api/post/")
    public String index(){
        return "index";
    }

}
