package ru.skillbox.diploma.controller;

import com.github.cage.GCage;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.model.Captcha;
import ru.skillbox.diploma.responce.CaptchaResponse;
import ru.skillbox.diploma.service.CaptchaService;
import ru.skillbox.diploma.service.UserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private CaptchaService captchaService;

    Logger logger = LoggerFactory.getLogger(ApiAuthController.class);

    @GetMapping("/check")
    public ResponseEntity<String> checkAuthorization(Model model){
        logger.trace("Request /api/auth/check");
        model.addAttribute("result", false);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() throws IOException {
        CaptchaResponse captchaResponse = captchaService.generateCaptcha();

        return new ResponseEntity<>(captchaResponse, HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping (value = "/login", method = RequestMethod.POST)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String create(@RequestBody UserData userData) {
        logger.trace("/api/auth/login\ne_mail " + userData.getE_mail() + "\npassword " + userData.getPassword());
        return "result : false";
    }
}

@NoArgsConstructor
@Data
class UserData{
    private String e_mail;
    private String password;
}
