package ru.skillbox.diploma.responce;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class CalendarResponce {
    private List years;
    private Map<String, Long> posts;
}
