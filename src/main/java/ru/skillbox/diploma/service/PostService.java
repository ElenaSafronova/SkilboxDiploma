package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.controller.ApiPostController;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.repository.PostRepository;
import ru.skillbox.diploma.responce.PostResponse;
import ru.skillbox.diploma.value.PostStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    Logger logger = LoggerFactory.getLogger(ApiPostController.class);

    final String RECENT = "recent";
    final String POPULAR = "popular";
    final String BEST = "best";
    final String EARLY = "early";

    public Post save(Post post){
        return postRepository.save(post);
    }

    public Optional<Post> findById(int id){
        return postRepository.findById(id);
    }

    public List<Post> findAll(){
        return (List<Post>) postRepository.findAll();
    }

    public List<Post> findAllByIsActiveAndStatusAndTimeLessThanEqual(byte isActive, PostStatus status, ZonedDateTime time, Pageable pageable){
        return postRepository.findAllByIsActiveAndStatusAndTimeLessThanEqual(isActive, status, time, pageable);
    }

    public List<PostResponse> findActiveAndAcceptedPosts(int offset, int limit, String mode) {
        Pageable pagingAndSorting = null;
        logger.trace("Request /api/post?offset=" + offset +
                "&limit="+ limit  + "&mode=" + mode);

        switch (mode) {
            case RECENT:
                logger.trace("posts Sort.by(\"time\").descending()");
                pagingAndSorting = PageRequest.of(offset, limit, Sort.by("time").descending());
                break;
            case POPULAR:
                logger.trace("CALL PostRepository method findPopular()");
                break;
            case BEST:
                logger.trace("CALL PostRepository method findBest()");
                break;
            case EARLY:
                logger.trace("posts Sort.by(\"time\")");
                pagingAndSorting = PageRequest.of(offset, limit, Sort.by("time"));
                break;
        }

        List<Post> postList = postRepository.findAllByIsActiveAndStatusAndTimeLessThanEqual(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now(),
                pagingAndSorting);
        List<PostResponse> postResponseList = new ArrayList<>();
        postList.forEach(post ->  postResponseList.add(new PostResponse(post)));

        return postResponseList;
    }
}
