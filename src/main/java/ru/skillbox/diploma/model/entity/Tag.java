package ru.skillbox.diploma.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255)
    @NotBlank(message = "name cannot be null or whitespace")
    private String name;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    List<Tag2Post> tag2Posts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable (name = "tag2post",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> posts;

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
