package ru.skillbox.diploma.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.AppProperties;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class InitPropService {
    @Autowired
    private AppProperties initProperties;

    public Map<String, String> getInitProp() {
        Map<String, String> data = Map.of(
                "title", initProperties.getPropertiesMap().get("TITLE"),
                "subtitle", initProperties.getPropertiesMap().get("SUBTITLE"),
                "phone", initProperties.getPropertiesMap().get("PHONE"),
                "email", initProperties.getPropertiesMap().get("EMAIL"),
                "copyright", initProperties.getPropertiesMap().get("COPYRIGHT"),
                "copyrightFrom", initProperties.getPropertiesMap().get("COPYRIGHT_FROM")
        );
        return data;
    }

    private String getUtf8(String rawString) {
        final Charset UTF_8 = Charset.forName("UTF-8");
        final Charset ISO = Charset.forName("ISO-8859-1");

        return new String(rawString.getBytes(ISO), UTF_8);
    }
}
