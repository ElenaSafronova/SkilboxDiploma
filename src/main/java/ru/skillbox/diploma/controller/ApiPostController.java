package ru.skillbox.diploma.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.responce.AllPostResponse;
import ru.skillbox.diploma.responce.PostResponse;
import ru.skillbox.diploma.service.PostService;

import java.util.List;


@RestController
public class ApiPostController {
    @Autowired
    private PostService postService;

    Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    @GetMapping("/api/post")
    public ResponseEntity<AllPostResponse> getAllPosts(@RequestParam int offset,
                                                       @RequestParam int limit,
                                                       @RequestParam String mode){
        List<PostResponse> postList = postService.findActiveAndAcceptedPosts(offset, limit, mode);
        return new ResponseEntity<>(new AllPostResponse(postList.size(), postList), HttpStatus.OK);
    }


}
