package ru.skillbox.diploma.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diploma.model.User;

@Data
@NoArgsConstructor
public class UserDto {
    private int id;
    private String name;

    public UserDto(User user){
        id = user.getId();
        name = user.getName();
    }
}
