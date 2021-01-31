package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.repository.PostRepository;
import ru.skillbox.diploma.response.AllPostResponse;
import ru.skillbox.diploma.response.CalendarResponce;
import ru.skillbox.diploma.response.PostResponse;
import ru.skillbox.diploma.value.PostStatus;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    Logger logger = LoggerFactory.getLogger(PostService.class);

    ZoneId zoneId = ZoneId.of( "UTC" );

    final String RECENT = "recent";
    final String POPULAR = "popular";
    final String BEST = "best";
    final String EARLY = "early";

    public Post save(Post post){
        return postRepository.save(post);
    }

    @Transactional
    public Post findById(int id){
        int newViewCount = postRepository.findById(id).getViewCount() + 1;
        logger.trace("update Post p set p.viewCount = " + newViewCount + " where p.id = " + id);
        postRepository.updateViewCount(id, newViewCount);
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

    public int countActiveAndAcceptedPosts(){
        logger.trace("count active and accepted posts");
        return postRepository.countByIsActiveAndStatusAndTimeLessThanEqual(
                                                        (byte) 1,
                                                        PostStatus.ACCEPTED,
                                                        ZonedDateTime.now()
        );
    }

    public AllPostResponse getActiveAndAcceptedPosts(int offset, int limit, String mode) {

        logger.trace("Request /api/post?offset=" + offset +
                "&limit="+ limit  + "&mode=" + mode);

        Pageable pagingAndSorting = definePagingAndSortingType(mode, offset, limit);

        Page<Post> postPage = findAllActivePosts(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now(),
                pagingAndSorting);

        int number = postPage.getNumber();
        int numberOfElements = postPage.getNumberOfElements();
        int size = postPage.getSize();
        int totalElements = (int) postPage.getTotalElements();
        int totalPages = postPage.getTotalPages();
        System.out.printf("page info - page number %s, numberOfElements: %s, size: %s, "
                        + "totalElements: %s, totalPages: %s%n",
                number, numberOfElements, size, totalElements, totalPages);

        List<PostResponse> postResponseList = new ArrayList<>();
        postPage.forEach(post ->  postResponseList.add(new PostResponse(post)));

        switch (mode) {
            case POPULAR:
                logger.trace("posts sorted by getPostComments().size()");
                postResponseList.stream()
                        .sorted(Comparator.comparingInt(PostResponse::getCommentCount));
                break;
            case BEST:
                logger.trace("posts sorted by getVotes().size() where value = 1");
                postResponseList.stream()
                        .sorted(Comparator.comparing(PostResponse::getLikeCount));
                break;
        }

        return new AllPostResponse(totalElements, postResponseList);
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

    public CalendarResponce findTotalPostsCount4EveryDay(String years) {
        List<Integer> yearList = new ArrayList<>();
        try{
            if (years.length() == 4 && Integer.parseInt(years) > 2005) {
                yearList.add(Integer.parseInt(years));
            }
//           добавить else if - сплит и трим годов в виде списка

            else{
                yearList.add(Year.now().getValue());
            }
        }
        catch (Exception ex){
            yearList.add(Year.now().getValue());
        }

        Map<String, Long> postsMap = new HashMap<>();

        for (int curYear : yearList) {
            LocalDateTime startDate = LocalDate
                    .parse(yearList.get(0) + "-01-01").atStartOfDay();
            LocalDateTime endDate = startDate.plusYears(1);

            logger.trace("get posts in time period:\nstartDate: " + startDate + "\n endDate: " + endDate);

            List<Post> postsByDay = postRepository.findAllByIsActiveAndStatusAndTimeBetween(
                    (byte) 1,
                    PostStatus.ACCEPTED,
                    startDate.atZone(zoneId),
                    endDate.atZone(zoneId),
                    null
            ).toList();

            postsMap = postsByDay.stream()
                    .collect(Collectors.groupingBy(
                            p -> p.getTime().toLocalDate()
                                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), Collectors.counting()));
        };

        return new CalendarResponce(yearList, postsMap);
    }

    public int countByModerationStatus(PostStatus postStatus) {
        logger.trace("count posts by status (countByModerationStatus): " + postStatus.toString());
        return postRepository.countByStatus(postStatus);
    }
}
