package ru.skillbox.diploma.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_moderator", length = 1)
    @NotNull(message = "isModerator cannot be null")
    private byte isModerator;

    @Column(name = "reg_time", updatable = false)
    @NotNull(message = "regTime (time of registration) cannot be null")
    private ZonedDateTime regTime;

    @NotBlank(message = "username cannot be null or whitespace")
    @Size(min=3, max=15)
    private String name;

    @Column(unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "email cannot be null or whitespace")
    private String email;

    @NotBlank(message = "password cannot be null or whitespace")
    private String password;

    @Transient
    private String confirmPassword;

    private String code;

    @Column(columnDefinition="Text")
    private String photo;

    @OneToMany(mappedBy = "moderator", cascade = CascadeType.ALL)
    private List<Post> moderatedPostsList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> postList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostComment> postCommentsList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Vote> postVotesList;

    public User(@NotNull(message = "isModerator cannot be null") byte isModerator,
                @NotBlank(message = "username cannot be null or whitespace")
                        @Size(min = 3, max = 15) String name,
                @Email(message = "Email should be valid")
                @NotBlank(message = "email cannot be null or whitespace") String email,
                @NotBlank(message = "password cannot be null or whitespace") String password) {
        this.isModerator = isModerator;
        this.name = name;
        this.email = email;
        this.password = password;
        this.setRegTime(ZonedDateTime.now());
    }
}
