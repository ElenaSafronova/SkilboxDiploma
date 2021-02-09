package ru.skillbox.diploma.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "global_settings")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GlobalSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "global_settings code can not be null")
    private String code;

    @NotNull(message = "global_settings name can not be null")
    private String name;

    @NotNull(message = "global_settings value can not be null")
    private String value;

}
