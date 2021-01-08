package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "post_votes") // лайки и дизлайки постов
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull(message = "userId cannot be null")
    @ManyToOne
    @JoinColumn(name ="user_id",
            referencedColumnName="id"
    )
    private User user;

    @NotNull(message = "postId cannot be null")
    @ManyToOne
    @JoinColumn(
            name = "post_id",
            referencedColumnName="id"
    )
    private Post post;

    @Column(updatable = false)
    @NotNull(message = "post vote time cannot be null")
    private LocalDateTime time;

    @NotNull(message = "value cannot be null")
    private byte value;
}
