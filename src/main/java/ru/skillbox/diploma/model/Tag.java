package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(length = 255)
    @NotBlank(message = "name cannot be null or whitespace")
    private String name;

}
