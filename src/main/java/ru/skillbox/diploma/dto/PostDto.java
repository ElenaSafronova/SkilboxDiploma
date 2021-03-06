package ru.skillbox.diploma.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.model.Vote;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private static final int ANNOUNCE_LENGTH = 100;

    private int id;
    private long timestamp;
    private UserWithPhotoDto user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;


    public PostDto(Post post) {
        this.id = post.getId();
        this.timestamp = Instant.from(post.getTime()).getEpochSecond();
        this.user = new UserWithPhotoDto(post.getUser());
        this.title = post.getTitle();
        this.announce = cutText(noHTMLString(post.getText()), ANNOUNCE_LENGTH);
        countLikesAndDislikes(post.getVotes());
        this.commentCount = post.getPostComments().size();
        this.viewCount = post.getViewCount();
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

    private String cutText(String text, int limit) {
        if (text.length() > limit){
            int newLimit = text.indexOf(' ', limit);
            return newLimit > limit
                    ? text.substring(0, newLimit) + "..."
                    : text.substring(0, limit);
        }
        else return text;
    }

    public static String noHTMLString(String html) {
        return html
                .replaceAll("\\<.*?>","")
                .replace("&nbsp;", " ");
    }
}
