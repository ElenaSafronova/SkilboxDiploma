package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag2post")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "postId cannot be null")
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull(message = "tagId cannot be null")
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public Tag2Post(@NotNull(message = "postId cannot be null") Post post,
                    @NotNull(message = "tagId cannot be null") Tag tag) {
        this.post = post;
        this.tag = tag;
    }
}
