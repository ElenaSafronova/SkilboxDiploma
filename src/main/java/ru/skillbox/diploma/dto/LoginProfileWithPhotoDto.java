package ru.skillbox.diploma.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LoginProfileWithPhotoDto extends LoginProfileDto{
    private MultipartFile photo;
}
