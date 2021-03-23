package ru.skillbox.diploma.model.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class TagDto {
    private String name;
    private Double weight;

    public TagDto(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public void setWeight(Double weight) {
        this.weight = new BigDecimal(weight).setScale(2, RoundingMode.HALF_UP).doubleValue();;
    }
}


