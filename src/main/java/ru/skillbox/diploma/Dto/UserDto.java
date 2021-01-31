package ru.skillbox.diploma.Dto;

import lombok.Data;
import ru.skillbox.diploma.model.User;

@Data
class UserResponse {
    private int id;
    private String name;

    public UserResponse(User user){
        id = user.getId();
        name = user.getName();
    }
}
