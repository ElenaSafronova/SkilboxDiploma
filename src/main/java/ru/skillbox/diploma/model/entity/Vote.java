package ru.skillbox.diploma.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "post_votes") // лайки и дизлайки постов
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "userId cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id",
            referencedColumnName="id"
    )
    private User user;

    @NotNull(message = "postId cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "post_id",
            referencedColumnName="id"
    )
    private Post post;

    @Column(updatable = false)
    @NotNull(message = "post vote time cannot be null")
    private ZonedDateTime time;

    @NotNull(message = "value cannot be null")
    private byte value;

    public Vote(@NotNull(message = "userId cannot be null") User user,
                @NotNull(message = "postId cannot be null") Post post,
                @NotNull(message = "value cannot be null") byte value) {
        this.user = user;
        this.post = post;
        this.value = value;
        this.time = ZonedDateTime.now();
    }
}
