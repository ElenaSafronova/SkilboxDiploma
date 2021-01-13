package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiAuthController {
    Logger logger = LoggerFactory.getLogger(ApiGeneralController.class);

    @RequestMapping("/api/auth/check")
    public ResponseEntity<String> checkAuthorization(Model model){
        logger.trace("Request /api/auth/check");
        model.addAttribute("result", false);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
