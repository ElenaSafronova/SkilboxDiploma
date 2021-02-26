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
import ru.skillbox.diploma.dto.*;
import ru.skillbox.diploma.model.*;
import ru.skillbox.diploma.repository.GlobalSettingRepository;
import ru.skillbox.diploma.repository.PostRepository;
import ru.skillbox.diploma.repository.VoteRepository;
import ru.skillbox.diploma.value.GlobalSettingCode;
import ru.skillbox.diploma.value.GlobalSettingValue;
import ru.skillbox.diploma.value.PostStatus;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PostService {

    @Autowired
    private AuthService authService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GlobalSettingRepository globalSettingRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private GlobalSettingsService globalSettingsService;

    @Autowired
    private TagService tagService;

    @Autowired
    private Tag2PostService tag2PostService;

    Logger logger = LoggerFactory.getLogger(PostService.class);

    ZoneId zoneId = ZoneId.of( "UTC" );

    final String RECENT = "recent";
    final String POPULAR = "popular";
    final String BEST = "best";
    final String EARLY = "early";

    final String INACTIVE = "inactive";
    final String PENDING = "pending";
    final String DECLINED = "declined";
    final String PUBLISHED = "published";

    final String NEW = "new";
    final String ACCEPTED = "accepted";

    public Post save(Post post){
        return postRepository.save(post);
    }

    public Post findById(int id){
        return postRepository.findById(id);
    }

    @Transactional
    public Post findByIdAndIncrementView(int id) {
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
        return postRepository.findAllByIsActiveAndStatusAndTimeLessThanEqual(
                isActive, status, time, pageable);
    }

    private Page<Post> findUserPosts(User user, Pageable pageable)
    {
        return postRepository.findAllByUser(user, pageable);
    }

    private Page<Post> findModerationPosts(User user, Pageable pagingAndSorting) {
        Set<User> moderators = new HashSet<>(){{
            add(user);
            add(null);
        }};
        return postRepository.findByModerator(moderators, pagingAndSorting);
    }

    private Page<Post> findAllByModeratorAndIsActive(User moderator, byte isActive, Pageable pageable)
    {
        return postRepository.findAllByModeratorAndIsActive(moderator, isActive, pageable);
    }

    private Page<Post> findAllByStatus(PostStatus status,
                                       Pageable pagingAndSorting) {
        return postRepository.findAllByStatus(status, pagingAndSorting);
    }

    private Page<Post> findAllByModeratorAndStatus(User moderator,
                                                   PostStatus status,
                                                   Pageable pagingAndSorting) {
        return postRepository.findAllByModeratorAndStatus(moderator, status, pagingAndSorting);
    }

    private Page<Post> findAllByModeratorAndIsActiveAndStatus(User moderator,
                                                              byte isActive,
                                                              PostStatus status,
                                                              Pageable pagingAndSorting) {
        return postRepository.findAllByModeratorAndIsActiveAndStatus(
                moderator, isActive, status, pagingAndSorting);
    }


    public AllPostDto searchPosts(byte isActive,
                                  PostStatus status,
                                  ZonedDateTime time,
                                  String text,
                                  Pageable pageable
    ){
        Page<Post> postList =  postRepository.findAllByIsActiveAndStatusAndTimeLessThanEqualAndTextContaining(
                isActive, status, time, text, pageable);

        List<PostDto> postDtoList = new ArrayList<>();
        postList.forEach(post ->  postDtoList.add(new PostDto(post)));

        return new AllPostDto((int) postList.getTotalElements(), postDtoList);
    }

    public int countActiveAndAcceptedPosts(){
        logger.trace("count active and accepted posts");
        return postRepository.countByIsActiveAndStatusAndTimeLessThanEqual(
                                                        (byte) 1,
                                                        PostStatus.ACCEPTED,
                                                        ZonedDateTime.now()
        );
    }

    public AllPostDto getActiveAndAcceptedPosts(int offset, int limit, String mode) {

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

        List<PostDto> postDtoList = new ArrayList<>();
        postPage.forEach(post ->  postDtoList.add(new PostDto(post)));

        switch (mode) {
            case POPULAR:
                logger.trace("posts sorted by getPostComments().size()");
                postDtoList.stream()
                        .sorted(Comparator.comparingInt(PostDto::getCommentCount));
                break;
            case BEST:
                logger.trace("posts sorted by getVotes().size() where value = 1");
                postDtoList.stream()
                        .sorted(Comparator.comparing(PostDto::getLikeCount));
                break;
        }

        return new AllPostDto(totalElements, postDtoList);
    }

    public AllPostDto getMyPosts(User user, int offset, int limit, String status) {

        logger.trace("Request /api/post/my?offset=" + offset +
                "&limit="+ limit  + "&status=" + status);

        Page<Post> postPage;
        Pageable pagingAndSorting = definePagingAndSortingType(status, offset, limit);
        List<PostDto> postDtoList = new ArrayList<>();

        switch (status) {
            case INACTIVE:
                logger.trace("posts sorted by is_active = 0");
                postPage = findAllByModeratorAndIsActive(user, (byte) 0, pagingAndSorting);
                break;
            case PENDING:
                logger.trace("posts sorted by is_active = 1, moderation_status = NEW");
                postPage = findAllByModeratorAndIsActiveAndStatus(
                        user, (byte) 1,
                        PostStatus.NEW,
                        pagingAndSorting);
                break;
            case DECLINED:
                logger.trace("posts sorted by is_active = 1, moderation_status = DECLINED");
                postPage = findAllByModeratorAndIsActiveAndStatus(
                        user, (byte) 1,
                        PostStatus.DECLINED,
                        pagingAndSorting);
                break;
            case PUBLISHED:
                logger.trace("posts sorted by is_active = 1, moderation_status = ACCEPTED");
                postPage = findAllByModeratorAndIsActiveAndStatus(
                        user, (byte) 1,
                        PostStatus.ACCEPTED,
                        pagingAndSorting);
                break;
            default:
                return new AllPostDto(0, postDtoList);
        }
        postPage.forEach(post ->  postDtoList.add(new PostDto(post)));
        return new AllPostDto((int) postPage.getTotalElements(), postDtoList);
    }

    public AllPostDto getModerationPosts(User user, int offset, int limit, String status) {

        logger.trace("Request /api/post/moderation?offset=" + offset +
                "&limit="+ limit  + "&status=" + status);
        Page<Post> postPage;
        Pageable pagingAndSorting = definePagingAndSortingType(status, offset, limit);

        List<PostDto> postDtoList = new ArrayList<>();

        switch (status) {
            case NEW:
                logger.trace("posts новые, необходима модерация");
                postPage = findAllByStatus(PostStatus.NEW, pagingAndSorting);
                break;
            case DECLINED:
                logger.trace("posts отклонённые мной");
                postPage = findAllByModeratorAndStatus(user, PostStatus.DECLINED, pagingAndSorting);
                break;
            case ACCEPTED:
                logger.trace("posts утверждённые мной");
                postPage = findAllByModeratorAndStatus(user, PostStatus.ACCEPTED, pagingAndSorting);
                break;
            default:
                return new AllPostDto(0, postDtoList);
        }
        postPage.stream()
                .forEach(post ->  postDtoList.add(new PostDto(post)));
        return new AllPostDto((int) postPage.getTotalElements(), postDtoList);
    }

    public AllPostDto getPostsByDate(int offset, int limit, ZonedDateTime dateStart, ZonedDateTime dateFinish){
        Page<Post> postPage = postRepository.findAllByIsActiveAndStatusAndTimeBetween(
                (byte) 1,
                PostStatus.ACCEPTED,
                dateStart, dateFinish,
                PageRequest.of(offset/10, limit));

//        Page<Post> postPage = postRepository.findPostsByDate(dateStart, dateFinish, PageRequest.of(offset, limit));

        List<PostDto> postDtoList = new ArrayList<>();
        postPage.forEach(post ->  postDtoList.add(new PostDto(post)));
        return new AllPostDto((int) postPage.getTotalElements(), postDtoList);
    }

    private Pageable definePagingAndSortingType(String mode, int offset, int limit) {
        Pageable pagingAndSorting;
        offset = offset / 10;
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

    public AllPostDto findPostsByTag(int offset, int limit, String tag) {
//        Post curPost = postRepository.findById(2).get();
//        System.out.println(curPost.getTags());

        Page<Post> postPage = postRepository
                .findAllByIsActiveAndStatusAndTimeLessThanEqualAndTags_NameContaining(
                    (byte) 1,
                    PostStatus.ACCEPTED,
                    ZonedDateTime.now(),
                    tag,
                    PageRequest.of(offset/10, limit));

        List<PostDto> postDtoList = new ArrayList<>();
        postPage.forEach(post ->  postDtoList.add(new PostDto(post)));
        return new AllPostDto((int) postPage.getTotalElements(), postDtoList);
    }

    public CalendarDto findTotalPostsCount4EveryDay(String years) {
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

        return new CalendarDto(yearList, postsMap);
    }

    public int countByModerationStatus(PostStatus postStatus) {
        logger.trace("count posts by status (countByModerationStatus): " + postStatus.toString());
        return postRepository.countByStatusAndIsActive(postStatus, (byte) 1);
    }

    public StatisticsDto getStatisticsAll() {
        logger.info(globalSettingsService.findByCode(GlobalSettingCode.STATISTICS_IS_PUBLIC.name()).toString());
        // В случае, если
        // публичный показ статистики блога запрещён
        // (см. соответствующий параметр в global_settings)
        // и текущий пользователь не модератор, должна выдаваться ошибка 401
         if (globalSettingsService
                .findByCode(GlobalSettingCode.STATISTICS_IS_PUBLIC
                        .name()).getValue()
                .equals(GlobalSettingValue.NO.name()))
        {
            if (authService.getCurUser().getIsModerator() != 1){
                return null;
            }
        }
        int postsCount = postRepository.countByIsActiveAndStatusAndTimeLessThanEqual(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now());
        int likes = voteRepository.countByValue((byte) 1);
        int dislikes = voteRepository.countByValue((byte) -1);
        int viewCount = postRepository.viewCountSum(ZonedDateTime.now());
        long firstPublication = Instant.from(postRepository.findPostListByIsActiveAndStatusAndTimeLessThanEqual(
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now()
        ).get(0).getTime()).getEpochSecond();
        return new StatisticsDto(postsCount, likes, dislikes, viewCount, firstPublication);
    }

    public StatisticsDto getStatisticsMy() {
        int viewCount = 0;
        User curUser = authService.getCurUser();
        List<Post> curUserPosts = postRepository.findPostListByUserAndIsActiveAndStatusAndTimeLessThanEqual(
                curUser,
                (byte) 1,
                PostStatus.ACCEPTED,
                ZonedDateTime.now()
        );
        int postsCount = curUserPosts.size();
        int likes = voteRepository.countByUserAndValue(curUser, (byte) 1);
        int dislikes = voteRepository.countByUserAndValue(curUser, (byte) -1);
        for (Post post : curUserPosts){
            viewCount += post.getViewCount();
        }
        long firstPublication = Instant.from(curUserPosts.get(0).getTime()).getEpochSecond();
        return new StatisticsDto(postsCount, likes, dislikes, viewCount, firstPublication);
    }

    public boolean vote(byte value, User curUser, Post curPost) {
        Vote voteFromDB = voteRepository.findByUserAndPost(curUser, curPost);
        if (voteFromDB == null){
            voteRepository.save(new Vote(curUser, curPost, value));
            return true;
        }
        else{
            if (voteFromDB.getValue() != value){
                voteFromDB.setValue(value);
                voteRepository.save(voteFromDB);
                return true;
            }
        }
        return false;
    }


    public ResultAndErrorDto addPost(long timestamp, int isActive,
                                     String title, List<String> tags, String text) {
        logger.trace("addPost method called");
//        Многопользовательский режим
//Если галочка не отмечена, публиковать посты может только модератор. Если отмечена - любой зарегистрированный пользователь
        if(globalSettingsService
                .findByCode(GlobalSettingCode.MULTIUSER_MODE.name())
                .getValue()
                .equals(GlobalSettingValue.YES.name())
        || authService.getCurUser().getIsModerator() == 1){
            Map<String, String> errors = findNewPostErrors(title, text);
            if (errors.size() > 0){
                return new ResultAndErrorDto(false, errors);
            }

            Post newPost = new Post((byte) isActive,
                    ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()),
                    title, text, authService.getCurUser());
            postRepository.save(newPost);
            logger.info("new Post in DB: " + newPost);

            tags.forEach(tag -> {
                Tag tagFromDB = tagService.findByName(tag);
                Tag2Post tag2Post = new Tag2Post(newPost, tagFromDB);
                tag2PostService.save(tag2Post);
                logger.info("new tag2PostService in DB: " + tag2Post);
            });
            return new ResultAndErrorDto(true, null);
        }
        return new ResultAndErrorDto(false, null);
    }

    public ResultAndErrorDto modifyPost(int postId, long timestamp, int isActive,
                                        String title, List<String> tags, String text) {
        logger.trace("modifyPost method called");
        Map<String, String> errors = findNewPostErrors(title, text);
        if (errors.size() > 0){
            return new ResultAndErrorDto(false, errors);
        }

        Post curPost = findById(postId);
        if (curPost == null){
            return new ResultAndErrorDto(false, errors);
        }
        curPost.setTime(ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()));
        curPost.setIsActive((byte) isActive);
        curPost.setTitle(title);
        curPost.setText(text);
        postRepository.save(curPost);
        logger.info("modified Post in DB: " + curPost);

        //TODO:
        tags.forEach(tag -> {
            System.out.println(tag);
            Tag tagFromDB = tagService.findByName(tag);
            System.out.println(tagFromDB);
            Tag2Post tag2Post = new Tag2Post(curPost, tagFromDB);
            tag2PostService.save(tag2Post);
            logger.info("new tag2PostService in DB: " + tag2Post);
        });
        return new ResultAndErrorDto(true, null);
    }

    private Map<String, String> findNewPostErrors(String title, String text) {
        Map<String, String> errors = new HashMap<>();
        if (title.isBlank() || title.length() < 3){
            errors.put("title", "Заголовок не установлен");
        }
        if (text.isBlank() || text.length() < 50){
            errors.put("text", "Текст публикации слишком короткий");
        }
        return errors;
    }

    public ResultDto moderate(Map<String, String> request) {
        User curUser = authService.getCurUser();
        int postId = Integer.parseInt(request.get("post_id"));
        String decision = request.get("decision");
        if(curUser != null && curUser.getIsModerator() == 1){
            Post curPost = findById(postId);
            if (decision.equals("accept")) {
                curPost.setStatus(PostStatus.ACCEPTED);
            } else if (decision.equals("decline")) curPost.setStatus(PostStatus.DECLINED);
            curPost.setModerator(curUser);  // модератор, принявший решение
            save(curPost);
            logger.info(decision + " post #" + postId);
            return new ResultDto(true);
        }
        return new ResultDto(false);
    }
}