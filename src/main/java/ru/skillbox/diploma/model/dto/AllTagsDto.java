package ru.skillbox.diploma.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AllTagsDto {
    private List<TagDto> tags;
}
