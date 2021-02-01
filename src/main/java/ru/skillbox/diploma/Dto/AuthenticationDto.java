package ru.skillbox.diploma.Dto;

import lombok.Data;

@Data
public class AuthenticationDto {
    private boolean result;
    private UserDataAuthDto user;

    public AuthenticationDto(UserDataAuthDto userDataAuthDto) {
        this.user = userDataAuthDto;
        this.result = userDataAuthDto != null;
    }
}
