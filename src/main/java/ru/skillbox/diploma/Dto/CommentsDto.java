package ru.skillbox.diploma.Dto;

import lombok.Data;
import ru.skillbox.diploma.model.PostComment;
import ru.skillbox.diploma.model.User;

import java.time.Instant;

@Data
public class CommentsDto {
    private int id;
    private long timestamp;
    private String text;
    private UserDto user;

    public CommentsDto(PostComment comment){
        this.id = comment.getId();
        this.timestamp = Instant.from(comment.getTime()).getEpochSecond();
        this.text = comment.getText();
        this.user = new UserDto(comment.getUser());
    }
}

@Data
class UserDto {
    private int id;
    private String name;
    private String photo;

    UserDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.photo = user.getPhoto();
    }

}
