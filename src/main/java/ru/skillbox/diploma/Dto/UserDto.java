package ru.skillbox.diploma.Dto;

import lombok.Data;
import ru.skillbox.diploma.model.User;

@Data
public class UserDto {
    private int id;
    private String name;

    public UserDto(User user){
        id = user.getId();
        name = user.getName();
    }
}
