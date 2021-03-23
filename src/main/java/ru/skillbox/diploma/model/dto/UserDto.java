package ru.skillbox.diploma.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diploma.model.entity.User;

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
