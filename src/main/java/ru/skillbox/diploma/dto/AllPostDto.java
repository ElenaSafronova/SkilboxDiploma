package ru.skillbox.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllPostDto {
    private int count;
    private List<PostDto> posts;
}
