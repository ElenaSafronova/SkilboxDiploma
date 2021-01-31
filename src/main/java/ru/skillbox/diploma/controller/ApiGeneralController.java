package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.model.GlobalSetting;
import ru.skillbox.diploma.repository.GlobalSettingRepository;
import ru.skillbox.diploma.response.AllTagsResponce;
import ru.skillbox.diploma.response.CalendarResponce;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.service.TagService;
import ru.skillbox.diploma.value.GlobalSettingsValue;

import java.util.*;

@RestController
public class ApiGeneralController {
    Logger logger = LoggerFactory.getLogger(ApiGeneralController.class);

    @Autowired
    private GlobalSettingRepository globalSettingRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    final String TITLE = "DevPub";
    final String SUBTITLE = "Рассказы разработчиков";
    final String PHONE = "+7 903 666-44-55";
    final String EMAIL = "mail@mail.ru";
    final String COPYRIGHT = "Дмитрий Сергеев";
    final String COPYRIGHT_FROM = "2005";

    @GetMapping(value = "/api/init", produces = "application/json")
    public Map<String, String> getInitData(){
        logger.trace("Request /api/init");

        Map<String, String> data = new HashMap<>();
        data.put("title", TITLE);
        data.put("subtitle", SUBTITLE);
        data.put("phone", PHONE);
        data.put("email", EMAIL);
        data.put("copyright", COPYRIGHT);
        data.put("copyrightFrom", COPYRIGHT_FROM);

        return data;
    }


    @GetMapping("/api/settings")
    public Map<String, Boolean> getGlobalSettings(){
        logger.trace("Request /api/globalSettings");

        Map<String, Boolean> settings = new HashMap<>();
        Iterator<GlobalSetting> iter = globalSettingRepository.findAll().iterator();
        while(iter.hasNext())
        {
            GlobalSetting curSettings = iter.next();
            if (curSettings.getValue().equals(GlobalSettingsValue.YES.name())) {
                settings.put(curSettings.getCode(), true);
            } else if (curSettings.getValue().equals(GlobalSettingsValue.NO.name())) {
                settings.put(curSettings.getCode(), false);
            }
        }
        return settings;
    }

    @GetMapping("/api/tag")
    public AllTagsResponce getTags(@RequestParam(required = false) String query) {
        logger.trace("Request /api/tag");

        return new AllTagsResponce(tagService.findTagsWithWeight(query == null ? "" : query));
    }

    @GetMapping("/api/calendar")
    public CalendarResponce getPosts4Calendar(@RequestParam(required = false) String years) {
        logger.trace("Request /api/calendar");

        return postService.findTotalPostsCount4EveryDay(years);
    }
}
