package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.dto.*;
import ru.skillbox.diploma.exception.EmailExistsException;
import ru.skillbox.diploma.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private PostService postService;

    @Autowired
    private GeneralService generalService;

    @GetMapping("/check")
    public ResponseEntity<AuthenticationDto> checkAuthorization(
            @Header("simpSessionId") String sessionId,
            HttpSession session
    )
    {
        LOGGER.trace("Request /api/auth/check");
        LOGGER.trace("HttpSessionId: " + session.getId());

        return new ResponseEntity<>(authService.checkAuthentication(), HttpStatus.OK);
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {
        CaptchaDto captchaDto = captchaService.generateCaptcha();
        return new ResponseEntity<>(captchaDto, HttpStatus.OK);
    }

    @GetMapping("/login")
    public void login(){
        LOGGER.trace("GET /api/auth/login");
        LOGGER.trace("login?error");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> authUser(@RequestBody LoginDto loginDto) {
        LOGGER.trace("POST /api/auth/login");
        LOGGER.trace("loginDto = " + loginDto.toString());
        UserDataAuthDto userDataAuthDto = authService.login(loginDto);

        return new ResponseEntity<>(new AuthenticationDto(userDataAuthDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ResultAndErrorDto> registerUser(@RequestBody NewUserDataDto newUserDataDto) throws EmailExistsException {
        LOGGER.trace("/api/auth/register");
        ResultAndErrorDto resultAndErrorDto = authService.registerNewUser(newUserDataDto);
        return new ResponseEntity(resultAndErrorDto, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResultDto> logOut(HttpServletRequest request,
                                            HttpServletResponse response) throws URISyntaxException {
        LOGGER.trace("/api/auth/logout");
        authService.logout(request, response);

//        URI uri = new URI("/");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
//        response.setHeader("Location", "/");

        return new ResponseEntity<>(new ResultDto(true), HttpStatus.OK);
    }

    @PostMapping("/restore")
    public ResponseEntity<ResultDto> restorePassword(
            @RequestBody HashMap<String ,String> emailResp){
        LOGGER.trace("/api/auth/restore");
        String email = emailResp.get("email");
        boolean result = generalService.sendEmailToUser(email);
        return new ResponseEntity<>(new ResultDto(result), HttpStatus.OK);
    }
}

