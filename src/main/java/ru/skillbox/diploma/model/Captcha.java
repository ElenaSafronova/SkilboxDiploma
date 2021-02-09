package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "captcha_codes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Captcha {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(updatable = false)
    @NotNull(message = "time cannot be null")
    private LocalDateTime time;

    @NotNull(message = "code cannot be null")
    private String code;

    @Column(name = "secret_code")
    @NotNull(message = "secretCode cannot be null")
    private String secretCode;

    public Captcha(@NotNull(message = "code cannot be null") String code,
                   @NotNull(message = "secretCode cannot be null") String secretCode) {
        this.code = code;
        this.secretCode = secretCode;
        this.time = LocalDateTime.now();
    }
}
