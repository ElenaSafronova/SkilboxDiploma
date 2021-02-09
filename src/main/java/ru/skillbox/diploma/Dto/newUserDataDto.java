package ru.skillbox.diploma.Dto;

import lombok.Data;
import ru.skillbox.diploma.model.User;

@Data
public class newUserDataDto {
    private String e_mail;
    private String password;
    private String name;
    private String captcha;
    private String captcha_secret;
}
