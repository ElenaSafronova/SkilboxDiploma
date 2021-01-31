package ru.skillbox.diploma.response;

import lombok.Data;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.model.PostComment;
import ru.skillbox.diploma.model.User;

import java.time.Instant;

@Data
public class CommentsResponse {
    private int id;
    private long timestamp;
    private String text;
    private UserResp user;

    public CommentsResponse(PostComment comment){
        this.id = comment.getId();
        this.timestamp = Instant.from(comment.getTime()).getEpochSecond();
        this.text = comment.getText();
        this.user = new UserResp(comment.getUser());
    }
}

@Data
class UserResp{
    private int id;
    private String name;
    private String photo;

    UserResp(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.photo = user.getPhoto();
    }

}
