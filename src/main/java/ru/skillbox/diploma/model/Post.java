package ru.skillbox.diploma.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.diploma.value.PostStatus;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "isActive cannot be null")
    @Column(name = "is_active", length = 1)
    private byte isActive;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(8) default 'NEW'", name = "moderation_status")
    @NotNull(message = "status cannot be null")
    private PostStatus status;

    @ManyToOne
    @JoinColumn(
            name ="moderator_id",
            referencedColumnName="id"
    )
    private User moderator;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName="id"
    )
    @NotNull(message = "userId cannot be null")
    private User user;

    @Column(updatable = false)
    @NotNull(message = "post creating time cannot be null")
    private ZonedDateTime time;

    @Column()
    @NotNull(message = "title cannot be null")
    private String title;

    @Column(length = 65535, columnDefinition = "text")
    @NotNull(message = "text cannot be null")
    private String text;

    @Column(name = "view_count")
    @NotNull(message = "viewCount cannot be null")
    private int viewCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    List<Tag2Post> tag2Posts;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable (name = "tag2post",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags = new ArrayList<>();
}

