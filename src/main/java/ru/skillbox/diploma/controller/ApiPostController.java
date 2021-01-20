package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.responce.AllPostResponse;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.value.PostStatus;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@RestController
public class ApiPostController {
    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    @GetMapping("/api/post")
    public ResponseEntity<AllPostResponse> getAllPosts(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String mode){
        logger.trace("/api/post");
        AllPostResponse postResponse = postService.getActiveAndAcceptedPosts(offset, limit, mode);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/api/post/search")
    public ResponseEntity<AllPostResponse> getSearchedPosts(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String query){
        logger.trace("/api/post/search");
        AllPostResponse postResponse = postService.searchPosts(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now(),
                query,
                PageRequest.of(offset, limit));
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/api/post/byDate")
    public ResponseEntity<AllPostResponse> getPostsByDate(@RequestParam int offset,
                                                          @RequestParam int limit,
                                                          @RequestParam String date){
        LocalDate dateLocal = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime dateStart = dateLocal.atStartOfDay(ZoneOffset.UTC);
        ZonedDateTime dateFinish = dateLocal.atStartOfDay(ZoneOffset.UTC).plusDays(1);
        logger.trace("Request /api/post/byDate?offset=" + offset +
                "&limit="+ limit  + "&dateStart=" + dateStart + "&dateFinish=" + dateFinish);

        AllPostResponse postResponse = postService.getPostsByDate(offset, limit, dateStart, dateFinish);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/api/post/byTag")
    public ResponseEntity<AllPostResponse> getPostsByTag(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String tag){
        logger.trace("/api/post/byTag?tag=" + tag);
        AllPostResponse postResponse = postService.findPostsByTag(offset, limit, tag);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

}
