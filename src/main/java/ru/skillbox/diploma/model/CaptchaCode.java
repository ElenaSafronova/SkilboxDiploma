package ru.skillbox.diploma.model;

import jdk.jfr.BooleanFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "captcha_codes")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(updatable = false)
    @NotNull(message = "time cannot be null")
    private LocalDateTime time;

    @NotNull(message = "code cannot be null")
    private byte code;

    @Column(name = "secret_code")
    @NotNull(message = "secretCode cannot be null")
    private byte secretCode;
}
