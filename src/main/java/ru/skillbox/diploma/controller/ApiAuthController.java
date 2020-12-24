package ru.skillbox.diploma.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class ApiAuthController {
    @RequestMapping("/api/auth/")
    public String index(){
        return "index";
    }
}
