package ru.skillbox.diploma.model.dto;

import lombok.Data;

@Data
public class LoginProfileDto{
    private String email;
    private String password;
    private String name;
    private boolean removePhoto;
}
