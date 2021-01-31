package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.response.AllPostResponse;
import ru.skillbox.diploma.response.OnePostResponse;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.value.PostStatus;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    @GetMapping("")
    public ResponseEntity<AllPostResponse> getAllPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                                       @RequestParam(required = false, defaultValue = "10") int limit,
                                                       @RequestParam String mode){
        logger.trace("/api/post");
        AllPostResponse postResponse = postService.getActiveAndAcceptedPosts(offset/10, limit, mode);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<AllPostResponse> getSearchedPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                                       @RequestParam(required = false, defaultValue = "10") int limit,
                                                       @RequestParam String query){
        logger.trace("/api/post/search");
        AllPostResponse postResponse = postService.searchPosts(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now(),
                query,
                PageRequest.of(offset/10, limit));
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<AllPostResponse> getPostsByDate(@RequestParam(required = false, defaultValue = "0") int offset,
                                                          @RequestParam(required = false, defaultValue = "10") int limit,
                                                          @RequestParam String date){
        LocalDate dateLocal = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime dateStart = dateLocal.atStartOfDay(ZoneOffset.UTC);
        ZonedDateTime dateFinish = dateLocal.atStartOfDay(ZoneOffset.UTC).plusDays(1);
        logger.trace("Request /api/post/byDate?offset=" + offset +
                "&limit="+ limit  + "&dateStart=" + dateStart + "&dateFinish=" + dateFinish);

        AllPostResponse postResponse = postService.getPostsByDate(offset/10, limit, dateStart, dateFinish);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<AllPostResponse> getPostsByTag(@RequestParam(required = false, defaultValue = "0") int offset,
                                                       @RequestParam(required = false, defaultValue = "10") int limit,
                                                       @RequestParam String tag){
        logger.trace("/api/post/byTag?tag=" + tag);
        AllPostResponse postResponse = postService.findPostsByTag(offset/10, limit, tag);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnePostResponse> getPostByID(@PathVariable int id){
        logger.trace("/api/post/" + id);
        return new ResponseEntity<>(new OnePostResponse(postService.findById(id)), HttpStatus.OK);
    }

}
