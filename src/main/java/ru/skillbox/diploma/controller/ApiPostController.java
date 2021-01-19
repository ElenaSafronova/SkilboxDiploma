package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.responce.AllPostResponse;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.value.PostStatus;

import java.time.ZonedDateTime;


@RestController
public class ApiPostController {
    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    @GetMapping("/api/post")
    public ResponseEntity<AllPostResponse> getAllPosts(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String mode){
        AllPostResponse postResponse = postService.getActiveAndAcceptedPosts(offset, limit, mode);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/api/post/search")
    public ResponseEntity<AllPostResponse> getSearchedPosts(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String query){
        AllPostResponse postResponse = postService.searchPosts(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now(),
                query,
                PageRequest.of(offset, limit));
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

}
