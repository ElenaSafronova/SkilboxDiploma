package ru.skillbox.diploma.model.dto;

import lombok.Data;
import ru.skillbox.diploma.model.entity.PostComment;
import ru.skillbox.diploma.model.entity.User;

import java.time.Instant;

@Data
public class CommentsDto {
    private int id;
    private long timestamp;
    private String text;
    private UserWithPhotoDto user;

    public CommentsDto(PostComment comment){
        this.id = comment.getId();
        this.timestamp = Instant.from(comment.getTime()).getEpochSecond();
        this.text = comment.getText();
        this.user = new UserWithPhotoDto(comment.getUser());
    }
}

@Data
class UserWithPhotoDto {
    private int id;
    private String name;
    private String photo;

    UserWithPhotoDto(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.photo = "/img/userPhoto/" + user.getId() + "/" + user.getPhoto();
    }

}
