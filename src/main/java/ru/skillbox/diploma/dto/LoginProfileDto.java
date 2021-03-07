package ru.skillbox.diploma.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LoginProfileDto{
    private String email;
    private String password;
    private String name;
    private boolean removePhoto;
}
