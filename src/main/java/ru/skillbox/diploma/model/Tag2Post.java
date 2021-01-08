package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag2post")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull(message = "postId cannot be null")
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @NotNull(message = "tagId cannot be null")
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
