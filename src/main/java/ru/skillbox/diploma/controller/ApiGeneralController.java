package ru.skillbox.diploma.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.model.dto.*;
import ru.skillbox.diploma.service.*;

import java.util.*;

@RestController
public class ApiGeneralController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiGeneralController.class);

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    @Autowired
    private InitPropService initPropService;

    @Autowired
    private GeneralService generalService;


    @GetMapping(value = "/api/init", produces = "application/json")
    public Map<String, String> getInitData(){
        LOGGER.trace("Request /api/init");

        return initPropService.getInitProp();
    }

    @GetMapping("/api/settings")
    public Map<String, Boolean> getGlobalSettings(){
        LOGGER.trace("GetMapping /api/globalSettings");
        return globalSettingsService.getSettings();
    }

    @RequestMapping(value = "/api/settings", method={RequestMethod.POST,RequestMethod.PUT})
//    @ResponseBody
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public void setGlobalSettings(@RequestBody Map<String, Boolean> settings){
        LOGGER.trace("PostMapping /api/globalSettings");
        globalSettingsService.setSettings(settings);
    }

    @GetMapping("/api/tag")
    public AllTagsDto getTags(@RequestParam(required = false) String query) {
        LOGGER.trace("Request /api/tag");

        return new AllTagsDto(tagService.findTagsWithWeight(query == null ? "" : query));
    }

    @GetMapping("/api/calendar")
    public CalendarDto getPosts4Calendar(@RequestParam(required = false) String years) {
        LOGGER.trace("Request /api/calendar");
        return postService.findTotalPostsCount4EveryDay(years);
    }

    @GetMapping("/api/statistics/all")
    public ResponseEntity<StatisticsDto> getStatisticsAll() {
        LOGGER.trace("Request /api/statistics/all");
        StatisticsDto response = postService.getStatisticsAll();
        if (response == null){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/api/statistics/my")
    public ResponseEntity<StatisticsDto> getStatisticsMy() {
        LOGGER.trace("Request /api/statistics/my");
        StatisticsDto response = postService.getStatisticsMy();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/api/profile/my", consumes = "multipart/form-data")
    public ResponseEntity<ResultAndErrorDto> changePhoto(@ModelAttribute LoginProfileWithPhotoDto loginProfileDto){
        System.out.println(loginProfileDto.getPhoto());
        ResultAndErrorDto resultAndErrorDto = generalService.changeProfileWithPhoto(
                 loginProfileDto.getName(),
                 loginProfileDto.getEmail(),
                 loginProfileDto.getPassword(),
                 loginProfileDto.getPhoto()
         );
         return new ResponseEntity<>(resultAndErrorDto, HttpStatus.OK);
    }

    @PostMapping(value = "/api/profile/my", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultAndErrorDto> changeProfile(@RequestBody LoginProfileDto loginProfileDto){
        ResultAndErrorDto resultAndErrorDto =  generalService.changeProfile(
                loginProfileDto.getName(),
                loginProfileDto.getEmail(),
                loginProfileDto.getPassword(),
                loginProfileDto.isRemovePhoto()
        );
        return new ResponseEntity<>(resultAndErrorDto, HttpStatus.OK);
    }

    @PostMapping("api/moderation")
    @Secured("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<ResultDto> moderation(@RequestBody Map<String, String> request){
        LOGGER.trace("/api/moderation");
        ResultDto result = postService.moderate(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("api/comment")
    @Secured("hasRole('ROLE_USER')")
    public ResponseEntity<ResultAndErrorDto> comment(@RequestBody Map<String, String> request){
        LOGGER.trace("/api/comment");
        ResultAndErrorDto result = postService.comment(request);
        if (!result.isResult()){
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
