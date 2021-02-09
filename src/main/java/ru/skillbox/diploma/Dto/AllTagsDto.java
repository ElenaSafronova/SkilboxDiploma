package ru.skillbox.diploma.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AllTagsDto {
    private List<TagDto> tags;
}
