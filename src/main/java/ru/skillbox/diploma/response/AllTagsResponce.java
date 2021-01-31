package ru.skillbox.diploma.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class AllTagsResponce {
    private List<TagResponse> tags;
}
