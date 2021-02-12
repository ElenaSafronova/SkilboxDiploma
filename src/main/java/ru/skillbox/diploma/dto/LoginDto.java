package ru.skillbox.diploma.dto;

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
