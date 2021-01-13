package ru.skillbox.diploma.controller;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diploma.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ApiPostController {
    Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    final String RECENT = "recent";
    final String POPULAR = "popular";
    final String BEST = "best";
    final String EARLY = "early";

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/api/post")
    public ResponseEntity<JSONObject> getAllPosts(@RequestParam int offset,
                                      @RequestParam int limit,
                                      @RequestParam String mode){
        logger.trace("Request /api/post?offset=" + offset +
                "&limit="+ limit  + "&mode=" + mode);

        if (mode.equals(RECENT)){
            logger.trace("CALL PostRepository method findActive()");
        }
        else if (mode.equals(POPULAR)){
            logger.trace("CALL PostRepository method findPopular()");
        }
        else if (mode.equals(BEST)){
            logger.trace("CALL PostRepository method findBest()");
        }
        else if (mode.equals(EARLY)){
            logger.trace("CALL PostRepository method findEarly()");
        }

        Map<String, String> postAttrFromRepo = postRepository.findAllPosts();

        Map<String, Map<String, String>> postAttr = new HashMap<>();
//        postAttr.put("count", String.valueOf(postAttr.size()));
        postAttr.put("posts", postAttrFromRepo);


        return new ResponseEntity<>(new JSONObject(postAttr), HttpStatus.OK);
    }


//    @GetMapping("/api/post")
//    public ResponseEntity<String> getAllPosts(@RequestParam int offset,
//                                              @RequestParam int limit,
//                                              @RequestParam String mode,
//                                              Model model){
//        logger.trace("Request /api/post?offset=" + offset +
//                "&limit="+ limit  + "&mode=" + mode);
//
//        Iterable<Post> posts = postRepository.findAll();
//        model.addAttribute("posts", posts);
//        return new ResponseEntity<>("", HttpStatus.OK);
//    }

}
