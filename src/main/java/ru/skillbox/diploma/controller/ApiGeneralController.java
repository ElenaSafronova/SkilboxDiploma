package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.model.GlobalSetting;
import ru.skillbox.diploma.model.Tag;
import ru.skillbox.diploma.repository.GlobalSettingRepository;
import ru.skillbox.diploma.repository.TagRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ApiGeneralController {
    Logger logger = LoggerFactory.getLogger(ApiGeneralController.class);

    @Autowired
    private GlobalSettingRepository globalSettingRepository;

    @Autowired
    private TagRepository tagRepository;

    String title = "DevPub";
    String subtitle = "Рассказы разработчиков";
    String phone = "+7 903 666-44-55";
    String email = "mail@mail.ru";
    String copyright = "Дмитрий Сергеев";
    String copyrightFrom = "2005";

    @GetMapping(value = "/api/init", produces = "application/json")
    public Map<String, String> getInitData(){
        logger.trace("Request /api/init");

        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("subtitle", subtitle);
        data.put("phone", phone);
        data.put("email", email);
        data.put("copyright", copyright);
        data.put("copyrightFrom", copyrightFrom);

        return data;
    }


    @GetMapping("/api/settings")
    public Map<String, String> getGlobalSettings(){
        logger.trace("Request /api/globalSettings");

        Map<String, String> settings = new HashMap<>();
        Iterable<GlobalSetting> globalSettings = globalSettingRepository.findAll();
        globalSettings.forEach(s -> settings.put(s.getCode(), s.getValue()));
        return settings;
    }

    @GetMapping("/api/tag")
    public Iterable<Tag> getTags() {
        logger.trace("Request /api/tag");

        Iterable<Tag> tagsIterable = tagRepository.findAll();
        return tagsIterable;
    }
}
