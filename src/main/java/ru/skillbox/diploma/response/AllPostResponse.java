package ru.skillbox.diploma.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AllPostResponse {
    private int count;
    private List<PostResponse> posts;
}
