package ru.skillbox.diploma.responce;

import lombok.Data;
import ru.skillbox.diploma.model.Tag;

import java.text.DecimalFormat;

@Data
public class TagResponse {
    private String name;
    private String weight;

    public TagResponse(String name, double weight) {
        DecimalFormat dec = new DecimalFormat("#0.00");

        this.name = name;
        this.weight = dec.format(weight);

//        this.weight = Double.parseDouble(String.format("%.3f", weight));
    }
}


