package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.ServletContextResource;
import ru.skillbox.diploma.dto.*;
import ru.skillbox.diploma.service.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
        LOGGER.trace("Request /api/globalSettings");
        return globalSettingsService.getSettings();
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

    @PostMapping(value = "/api/profile/my", consumes = "multipart/form-data")
    public ResponseEntity<RegistrationDto> changePhoto(@ModelAttribute LoginProfileDto loginProfileDto){
        RegistrationDto registrationDto = generalService.changeProfileWithPhoto(
                 loginProfileDto.getName(),
                 loginProfileDto.getEmail(),
                 loginProfileDto.getPassword(),
                 loginProfileDto.getPhoto()
         );
         return new ResponseEntity<>(registrationDto, HttpStatus.OK);
    }

    @PostMapping(value = "/api/profile/my", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegistrationDto> changeProfile(@RequestBody LoginProfileDto loginProfileDto){
        RegistrationDto registrationDto =  generalService.changeProfile(
                loginProfileDto.getName(),
                loginProfileDto.getEmail(),
                loginProfileDto.getPassword()
        );
        return new ResponseEntity<>(registrationDto, HttpStatus.OK);
    }

    @RequestMapping("/img/userPhoto")
    @ResponseBody
    public ResponseEntity<Resource> getImageAsResource(HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "multipart/form-data");
        Resource resource =
                new ServletContextResource(servletContext, "");
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

//    @RequestMapping(value = "user/userPhoto/{userId}", method = RequestMethod.GET)
//    @ResponseBody
//    public ResponseEntity<InputStreamResource> downloadUserAvatarImage(@PathVariable int userId) {
//        String avatar = generalService.findUserAccountAvatarById(userId);
//
//        return ResponseEntity.ok()
//                .contentLength(avatar.length())
//                .contentType(MediaType.parseMediaType(avatar.getContentType()))
//                .body(new InputStreamResource(avatar.getInputStream()));
//    }
}
