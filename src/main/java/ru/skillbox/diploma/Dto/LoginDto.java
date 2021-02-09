package ru.skillbox.diploma.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public
class LoginDto {
    private String e_mail;
    private String password;
}
