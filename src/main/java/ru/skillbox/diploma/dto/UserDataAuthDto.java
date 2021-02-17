package ru.skillbox.diploma.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.diploma.model.User;

@Data
public
class UserDataAuthDto extends UserDto {
    private String photo;
    @JsonProperty("e_mail")
    private String email;
    private boolean moderation;
    private int moderationCount;
    private boolean settings;

    public UserDataAuthDto(User user) {
        super(user);
        String curPhoto = user.getPhoto();
        if (curPhoto == null || curPhoto.isEmpty() || curPhoto.isBlank()){
            this.photo = curPhoto;
        }
        else{
            this.photo = "/img/userPhoto/" + user.getId() + "/" + curPhoto;
        }
        this.email = user.getEmail();
        this.moderation = user.getIsModerator() == 1;
        this.moderationCount = moderation ? 1 : 0;
        this.settings = moderation;
    }
}
