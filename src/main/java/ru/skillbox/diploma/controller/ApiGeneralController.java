package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.dto.StatisticsDto;
import ru.skillbox.diploma.dto.AllTagsDto;
import ru.skillbox.diploma.dto.CalendarDto;
import ru.skillbox.diploma.service.GlobalSettingsService;
import ru.skillbox.diploma.service.InitPropService;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.service.TagService;

import java.util.*;

@RestController
public class ApiGeneralController {
    Logger logger = LoggerFactory.getLogger(ApiGeneralController.class);

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    @Autowired
    InitPropService initPropService;

    @GetMapping(value = "/api/init", produces = "application/json")
    public Map<String, String> getInitData(){
        logger.trace("Request /api/init");

        return initPropService.getInitProp();
    }


    @GetMapping("/api/settings")
    public Map<String, Boolean> getGlobalSettings(){
        logger.trace("Request /api/globalSettings");
        return globalSettingsService.getSettings();
    }

    @GetMapping("/api/tag")
    public AllTagsDto getTags(@RequestParam(required = false) String query) {
        logger.trace("Request /api/tag");

        return new AllTagsDto(tagService.findTagsWithWeight(query == null ? "" : query));
    }

    @GetMapping("/api/calendar")
    public CalendarDto getPosts4Calendar(@RequestParam(required = false) String years) {
        logger.trace("Request /api/calendar");
        return postService.findTotalPostsCount4EveryDay(years);
    }

    @GetMapping("/api/statistics/all")
    public ResponseEntity<StatisticsDto> getStatisticsAll() {
        logger.trace("Request /api/statistics/all");
        StatisticsDto response = postService.getStatisticsAll();
        if (response == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
