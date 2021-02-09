package ru.skillbox.diploma.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.Dto.AllPostDto;
import ru.skillbox.diploma.Dto.OnePostDto;
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
    public ResponseEntity<AllPostDto> getAllPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                                  @RequestParam(required = false, defaultValue = "10") int limit,
                                                  @RequestParam String mode){
        logger.trace("/api/post");
        AllPostDto postResponse = postService.getActiveAndAcceptedPosts(offset/10, limit, mode);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<AllPostDto> getSearchedPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                                       @RequestParam(required = false, defaultValue = "10") int limit,
                                                       @RequestParam String query){
        logger.trace("/api/post/search");
        AllPostDto postResponse = postService.searchPosts(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now(),
                query,
                PageRequest.of(offset/10, limit));
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/byDate")
    public ResponseEntity<AllPostDto> getPostsByDate(@RequestParam(required = false, defaultValue = "0") int offset,
                                                     @RequestParam(required = false, defaultValue = "10") int limit,
                                                     @RequestParam String date){
        LocalDate dateLocal = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZonedDateTime dateStart = dateLocal.atStartOfDay(ZoneOffset.UTC);
        ZonedDateTime dateFinish = dateLocal.atStartOfDay(ZoneOffset.UTC).plusDays(1);
        logger.trace("Request /api/post/byDate?offset=" + offset +
                "&limit="+ limit  + "&dateStart=" + dateStart + "&dateFinish=" + dateFinish);

        AllPostDto postResponse = postService.getPostsByDate(offset/10, limit, dateStart, dateFinish);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<AllPostDto> getPostsByTag(@RequestParam(required = false, defaultValue = "0") int offset,
                                                    @RequestParam(required = false, defaultValue = "10") int limit,
                                                    @RequestParam String tag){
        logger.trace("/api/post/byTag?tag=" + tag);
        AllPostDto postResponse = postService.findPostsByTag(offset/10, limit, tag);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnePostDto> getPostByID(@PathVariable int id){
        logger.trace("/api/post/" + id);
        return new ResponseEntity<>(new OnePostDto(postService.findById(id)), HttpStatus.OK);
    }

}
