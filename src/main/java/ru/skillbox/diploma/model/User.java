package ru.skillbox.diploma.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "is_moderator", length = 1)
    @NotNull(message = "isModerator cannot be null")
    private byte isModerator;

    @Column(name = "reg_time", updatable = false)
    @NotNull(message = "regTime (time of registration) cannot be null")
    private LocalDateTime regTime;

//    @Column(unique = true)
    @NotBlank(message = "username cannot be null or whitespace")
    @Size(min=3, max=15)
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "email cannot be null or whitespace")
    private String email;

    @NotBlank(message = "password cannot be null or whitespace")
    private String password;

    private String code;

    @Column(columnDefinition="Text")
    private String photo;

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL)
    private List<Post> moderatedPostList = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostComment> postCommentsList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Vote> postVotesList = new ArrayList<>();

}
