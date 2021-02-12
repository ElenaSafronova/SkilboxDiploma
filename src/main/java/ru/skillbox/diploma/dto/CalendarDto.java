package ru.skillbox.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CalendarDto {
    private List years;
    private Map<String, Long> posts;
}
