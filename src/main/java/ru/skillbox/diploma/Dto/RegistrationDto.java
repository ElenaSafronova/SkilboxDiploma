package ru.skillbox.diploma.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class RegistrationDto {
    private boolean result;
    private Map<String, String> errors;
}



