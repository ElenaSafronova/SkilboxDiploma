package ru.skillbox.diploma;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "config-properties")
public class AppProperties {
    private Map<String, String> propertiesMap;
}


