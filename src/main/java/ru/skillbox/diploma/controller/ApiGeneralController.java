package ru.skillbox.diploma.controller;

import org.springframework.web.bind.annotation.RequestMapping;

public class ApiGeneralController {
    @RequestMapping("/api/")
    public String index(){
        return "index";
    }
}
