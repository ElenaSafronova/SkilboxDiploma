package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "post_comments")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name="parent_id")
    private PostComment parentComment;

    @NotNull(message = "postId cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="post_id",
            referencedColumnName="id",
            updatable = false)
    private Post post;

    @NotNull(message = "userId cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id",
            referencedColumnName="id",
            updatable = false)
    private User user;

    @Column(updatable = false)
    @NotNull(message = "time cannot be null")
    private ZonedDateTime time;

    @Column(columnDefinition = "text")
    @NotNull(message = "text cannot be null")
    private String text;

    public PostComment(PostComment parentComment,
                       @NotNull(message = "postId cannot be null") Post post,
                       @NotNull(message = "userId cannot be null") User user,
                       @NotNull(message = "text cannot be null") String text)
    {
        this.parentComment = parentComment;
        this.post = post;
        this.user = user;
        this.text = text;
        this.time = ZonedDateTime.now();
    }
}
