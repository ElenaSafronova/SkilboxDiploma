package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.controller.ApiPostController;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.repository.PostRepository;
import ru.skillbox.diploma.responce.AllPostResponse;
import ru.skillbox.diploma.responce.PostResponse;
import ru.skillbox.diploma.value.PostStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    public Page<Post> findAllActivePosts(byte isActive,
                                         PostStatus status,
                                         ZonedDateTime time,
                                         Pageable pageable)
    {
        //        сохранить количество постов в классе GeneralResponse и сам общий лист постов
        return postRepository.findAllByIsActiveAndStatusAndTimeLessThanEqual(
                isActive, status, time, pageable);
    }

    public AllPostResponse searchPosts(byte isActive,
                                       PostStatus status,
                                       ZonedDateTime time,
                                       String text,
                                       Pageable pageable
    ){
        Page<Post> postList =  postRepository.findAllByIsActiveAndStatusAndTimeLessThanEqualAndTextContaining(
                isActive, status, time, text, pageable);

        List<PostResponse> postResponseList = new ArrayList<>();
        postList.forEach(post ->  postResponseList.add(new PostResponse(post)));

        return new AllPostResponse((int) postList.getTotalElements(), postResponseList);
    }


    public AllPostResponse getActiveAndAcceptedPosts(int offset, int limit, String mode) {

        logger.trace("Request /api/post?offset=" + offset +
                "&limit="+ limit  + "&mode=" + mode);

        Pageable pagingAndSorting = definePagingAndSortingType(mode, offset, limit);

        Page<Post> postList = findAllActivePosts(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now(),
                pagingAndSorting);

        List<PostResponse> postResponseList = new ArrayList<>();
        postList.forEach(post ->  postResponseList.add(new PostResponse(post)));

        switch (mode) {
            case POPULAR:
                logger.trace("posts sorted by getPostComments().size()");
                postResponseList.stream().sorted(Comparator.comparingInt(p -> p.getCommentCount()));
                break;
            case BEST:
                logger.trace("posts sorted by getVotes().size() where value = 1");
                postResponseList.stream().sorted(Comparator.comparing(p -> p.getLikeCount()));
                break;
        }

        return new AllPostResponse((int) postList.getTotalElements(), postResponseList);
    }

    public AllPostResponse getPostsByDate(int offset, int limit, ZonedDateTime dateStart, ZonedDateTime dateFinish){
        Page<Post> postPage = postRepository.findAllByIsActiveAndStatusAndTimeBetween(
                (byte) 1,
                PostStatus.ACCEPTED,
                dateStart, dateFinish,
                PageRequest.of(offset, limit));

//        Page<Post> postPage = postRepository.findPostsByDate(dateStart, dateFinish, PageRequest.of(offset, limit));

        List<PostResponse> postResponseList = new ArrayList<>();
        postPage.forEach(post ->  postResponseList.add(new PostResponse(post)));
        return new AllPostResponse((int) postPage.getTotalElements(), postResponseList);
    }

    private Pageable definePagingAndSortingType(String mode, int offset, int limit) {
        Pageable pagingAndSorting;
        switch (mode) {
            case RECENT:
                logger.trace("posts Sort.by(\"time\").descending()");
                pagingAndSorting = PageRequest.of(offset, limit, Sort.by("time").descending());
                break;
            case EARLY:
                logger.trace("posts Sort.by(\"time\")");
                pagingAndSorting = PageRequest.of(offset, limit, Sort.by("time"));
                break;
            default:
                logger.trace("CALL PostRepository default method");
                pagingAndSorting = PageRequest.of(offset, limit);
        }
        return pagingAndSorting;
    }

    public AllPostResponse findPostsByTag(int offset, int limit, String tag) {
//        Post curPost = postRepository.findById(2).get();
//        System.out.println(curPost.getTags());

        Page<Post> postPage = postRepository
                .findAllByIsActiveAndStatusAndTimeLessThanEqualAndTags_NameContaining(
                    (byte) 1,
                    PostStatus.ACCEPTED,
                    ZonedDateTime.now(),
                    tag,
                    PageRequest.of(offset, limit));

        List<PostResponse> postResponseList = new ArrayList<>();
        postPage.forEach(post ->  postResponseList.add(new PostResponse(post)));
        return new AllPostResponse((int) postPage.getTotalElements(), postResponseList);
    }
}
