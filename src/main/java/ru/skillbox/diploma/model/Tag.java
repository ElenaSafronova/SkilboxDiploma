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
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;
>>>>>>> dev

@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag {
    @Id
<<<<<<< HEAD
    @GeneratedValue(strategy = GenerationType.AUTO)
=======
    @GeneratedValue(strategy = GenerationType.IDENTITY)
>>>>>>> dev
    private int id;

    @Column(length = 255)
    @NotBlank(message = "name cannot be null or whitespace")
    private String name;

<<<<<<< HEAD
=======
    @OneToMany(mappedBy = "tag")
    List<Tag2Post> tag2Posts;

    @ManyToMany
    @JoinTable (name = "tag2post",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> posts;

>>>>>>> dev
}
