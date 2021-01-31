package ru.skillbox.diploma.Dto;

import lombok.Data;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.model.PostComment;
import ru.skillbox.diploma.model.Vote;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class OnePostDto {
    private int id;
    private long timestamp;
    private int active;
    private UserResponse user;
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
        this.user = new UserResponse(post.getUser());
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