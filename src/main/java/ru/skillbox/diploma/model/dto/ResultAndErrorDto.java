package ru.skillbox.diploma.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ResultAndErrorDto {
    private boolean result;
    private Map<String, String> errors;
}



