package ru.skillbox.diploma.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllPostDto {
    private int count;
    private List<PostDto> posts;
}
