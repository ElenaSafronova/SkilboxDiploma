package ru.skillbox.diploma.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diploma.model.entity.Post;
import ru.skillbox.diploma.model.entity.PostComment;
import ru.skillbox.diploma.model.entity.Vote;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnePostDto {
    private int id;
    private long timestamp;
    private int active;
    private UserWithPhotoDto user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private List<CommentsDto> comments;
    private List<String> tags = new ArrayList<>();

    public OnePostDto(Post post){
        this.id = post.getId();
        this.timestamp = Instant.from(post.getTime()).getEpochSecond();
        this.active = post.getIsActive();
        this.user = new UserWithPhotoDto(post.getUser());
        this.title = post.getTitle();
        this.text = post.getText();
        this.viewCount = post.getViewCount();
        this.comments = defineComments(post.getPostComments());
        System.out.println(this.toString());
        post.getTag2Posts().forEach(tag -> {
            this.tags.add(tag.getTag().getName());
        });
    }

    private List<CommentsDto> defineComments(List<PostComment> comments) {
        List<CommentsDto> commentsRespons = new ArrayList<>();
        comments.forEach(com -> commentsRespons.add(new CommentsDto(com)));
        return commentsRespons;
    }

    private void countLikesAndDislikes(List<Vote> votes) {
        int likes = 0;
        int dislikes = 0;
        for (Vote vote : votes){
            if (vote.getValue() == 1) {
                likes++;
            } else {
                dislikes++;
            }
        }
        this.likeCount = likes;
        this.dislikeCount = dislikes;
    }
}
