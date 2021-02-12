package ru.skillbox.diploma.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diploma.model.User;

@Data
public
class UserDataAuthDto {
    private int id;
    private String name;
    private String photo;
    @JsonProperty("e_mail")
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

    public UserDataAuthDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.photo = user.getPhoto();
        this.email = user.getEmail();
        this.moderation = user.getIsModerator() == 1;
        this.moderationCount = moderation ? 1 : 0;
        this.settings = moderation;
    }
}
