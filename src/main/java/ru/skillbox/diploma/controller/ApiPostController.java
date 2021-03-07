package ru.skillbox.diploma.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diploma.dto.AllPostDto;
import ru.skillbox.diploma.dto.OnePostDto;
import ru.skillbox.diploma.dto.ResultAndErrorDto;
import ru.skillbox.diploma.dto.ResultDto;
import ru.skillbox.diploma.model.User;
import ru.skillbox.diploma.service.AuthService;
import ru.skillbox.diploma.service.PostService;
import ru.skillbox.diploma.value.PostStatus;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    @GetMapping("")
    public ResponseEntity<AllPostDto> getAllPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                                  @RequestParam(required = false, defaultValue = "10") int limit,
                                                  @RequestParam String mode){
        logger.trace("/api/post");
        AllPostDto postResponse = postService.getActiveAndAcceptedPosts(offset, limit, mode);
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

        AllPostDto postResponse = postService.getPostsByDate(offset, limit, dateStart, dateFinish);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/byTag")
    public ResponseEntity<AllPostDto> getPostsByTag(@RequestParam(required = false, defaultValue = "0") int offset,
                                                    @RequestParam(required = false, defaultValue = "10") int limit,
                                                    @RequestParam String tag){
        logger.trace("/api/post/byTag?tag=" + tag);
        AllPostDto postResponse = postService.findPostsByTag(offset, limit, tag);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnePostDto> getPostByID(@PathVariable int id){
        logger.trace("/api/post/" + id);
        return new ResponseEntity<>(new OnePostDto(postService.findByIdAndIncrementView(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method={RequestMethod.POST, RequestMethod.PUT})
    @Secured("hasRole('ROLE_USER')")
    public ResponseEntity<ResultAndErrorDto> modify(@PathVariable int id,
                                                    @RequestBody OnePostDto newPost){
        logger.trace("modify method /api/post/" + id);
        ResultAndErrorDto result = postService.modifyPost(
                id,
                newPost.getTimestamp(),
                newPost.getActive(),
                newPost.getTitle(),
                newPost.getTags(),
                newPost.getText());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/like")
    @Secured("hasRole('ROLE_USER')")
    public ResponseEntity<ResultDto> like(@RequestBody Map<String, Integer> request){
        logger.trace("/api/post/like");
        int id = request.get("post_id");
        User curUser = authService.getCurUser();
        boolean result = false;
        if(curUser != null){
            result = postService.vote((byte) 1,
                    authService.getCurUser(),
                    postService.findById(id));
        }
        return new ResponseEntity<>(new ResultDto(result), HttpStatus.OK);
    }

    @PostMapping("/dislike")
    @Secured("hasRole('ROLE_USER')")
    public ResponseEntity<ResultDto> dislike(@RequestBody Map<String, Integer> request){
        logger.trace("/api/post/dislike");
        int id = request.get("post_id");
        User curUser = authService.getCurUser();
        boolean result = false;
        if(curUser != null){
            result = postService.vote((byte) -1,
                    authService.getCurUser(),
                    postService.findById(id));
        }
        return new ResponseEntity<>(new ResultDto(result), HttpStatus.OK);
    }

    @GetMapping("/my")
    @Secured("hasRole('ROLE_USER')")
    public ResponseEntity<AllPostDto> getMyPosts(@RequestParam(required = false, defaultValue = "0") int offset,
                                                  @RequestParam(required = false, defaultValue = "10") int limit,
                                                  @RequestParam String status){
        logger.trace("/api/post/my");
        AllPostDto postResponse = postService.getMyPosts(authService.getCurUser(), offset, limit, status);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @PostMapping("")
    @Secured("hasRole('ROLE_USER')")
    public ResponseEntity<ResultAndErrorDto> addPost(@RequestBody OnePostDto newPost){
        logger.trace("/api/post");
        ResultAndErrorDto result = postService.addPost(
                newPost.getTimestamp(),
                newPost.getActive(),
                newPost.getTitle(),
                newPost.getTags(),
                newPost.getText());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/moderation")
    @Secured("hasRole('ROLE_MODERATOR')")
    public ResponseEntity<AllPostDto> getMyModerationPosts(
            @RequestParam(required = false, defaultValue = "0") int offset,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam String status){
        logger.trace("/api/post/moderation");
        AllPostDto postResponse = postService.getModerationPosts(
                authService.getCurUser(), offset, limit, status);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }
}
