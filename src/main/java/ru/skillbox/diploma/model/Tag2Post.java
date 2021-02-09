package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
=======

import javax.persistence.*;
>>>>>>> dev
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tag2post")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag2Post {
    @Id
<<<<<<< HEAD
    @GeneratedValue(strategy = GenerationType.AUTO)
=======
    @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> dev
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
