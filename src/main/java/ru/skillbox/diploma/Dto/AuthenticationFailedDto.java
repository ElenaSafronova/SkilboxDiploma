package ru.skillbox.diploma.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthenticationFailedDto {
    private boolean result = false;
}
