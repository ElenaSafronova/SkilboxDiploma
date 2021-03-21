package ru.skillbox.diploma.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllPostDto {
    private int count;
    private List<PostDto> posts;
}
