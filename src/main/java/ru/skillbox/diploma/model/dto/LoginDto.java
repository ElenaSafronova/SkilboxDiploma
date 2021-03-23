package ru.skillbox.diploma.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public
class LoginDto {
    @JsonProperty("e_mail")
    private String email;
    private String password;
}
