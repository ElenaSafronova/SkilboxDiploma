package ru.skillbox.diploma.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.entity.Post;
import ru.skillbox.diploma.model.entity.Tag;
import ru.skillbox.diploma.model.entity.User;
import ru.skillbox.diploma.model.value.PostStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {

    Iterable<Post> findByTagsContaining(Tag curTag);

    Post findById(int id);

    List<Post> findPostListByIsActiveAndStatusAndTimeLessThanEqual(byte isActive,
                                                              PostStatus status,
                                                              ZonedDateTime time
    );

    List<Post> findPostListByUserAndIsActiveAndStatusAndTimeLessThanEqual(User user,
                                                                          byte isActive,
                                                                          PostStatus accepted,
                                                                          ZonedDateTime now);

    Page<Post> findAllByUser(User user, Pageable pageable);

    Page<Post> findAllByStatus(PostStatus status, Pageable pageable);

    @Query("select p from Post p where p.moderator in :ids")
    Page<Post> findByModerator(@Param("ids") Set<User> ids, Pageable pageable);

    @Query("select p from Post p where p.moderator = ?1 and p.status = ?2")
    Page<Post> findAllByModeratorAndStatus(User moderator,
                                           PostStatus status,
                                           Pageable pageable);
    @Query("select p from Post p " +
            "where p.isActive = ?1 " +
            "and p.status = ?2 " +
            "and p.time <= ?3 " +
            "ORDER BY SIZE(p.postComments) DESC")
    Page<Post> findAllByIsActiveAndStatusAndTimeLessThanEqualOrderByPostCommentsDesc(
            byte isActive,
            PostStatus status,
            ZonedDateTime time,
            Pageable pageable
    );

//    @Query(value = "select p.id, COALESCE(SUM(v.value),0) as likes " +
//            "from posts as p " +
//                "left join post_votes as v on v.post_id = p.id " +
//            "where p.is_active = '1' and p.moderation_status = 'ACCEPTED' " +
//            "group by p.id " +
//            "/*#pageable*/ " +
//            "order by likes desc",
//            countQuery = "select count(*) from posts as p " +
//                    "where p.is_active = '1' and p.moderation_status = 'ACCEPTED'",
//            nativeQuery = true)
        @Query("select p " +
                "from Post p " +
                "left join p.votes v " +
            "where p.isActive = ?1 and p.status = ?2 and p.time <= ?3 " +
            "group by p " +
            "order by sum(v.value) desc")
    Page<Post> findAllByIsActiveAndStatusAndTimeLessThanEqualOrderByVoteCount(
            byte isActive,
            PostStatus status,
            ZonedDateTime time,
            byte value,
            Pageable pageable
    );

    Page<Post> findAllByIsActiveAndStatusAndTimeLessThanEqualAndVotes_Value(
            byte isActive,
            PostStatus status,
            ZonedDateTime time,
            byte voteValue,
            Pageable pageable);

    Page<Post> findAllByModeratorAndIsActive(User moderator, byte isActive, Pageable pageable);

    Page<Post> findAllByModeratorAndIsActiveAndStatus(User moderator, byte isActive,
                                                      PostStatus status, Pageable pageable);

    Page<Post> findAllByIsActiveAndStatusAndTimeLessThanEqual(byte isActive,
                                                              PostStatus status,
                                                              ZonedDateTime time,
                                                              Pageable pageable
                                                              );

    Page<Post> findAllByIsActiveAndStatusAndTimeLessThanEqualAndTextContaining(
                                                              byte isActive,
                                                              PostStatus status,
                                                              ZonedDateTime time,
                                                              String text,
                                                              Pageable pageable
    );

    Page<Post> findAllByIsActiveAndStatusAndTimeBetween(byte isActive,
                                                      PostStatus status,
                                                      ZonedDateTime publicationTimeStart,
                                                      ZonedDateTime publicationTimeEnd,
                                                      Pageable pageable
    );


    Page<Post> findAllByIsActiveAndStatusAndTimeLessThanEqualAndTags_NameContaining(
            byte isActive,
            PostStatus status,
            ZonedDateTime time,
            String tag,
            Pageable pageable
    );

    int countByStatus(PostStatus postStatus);

    int countByStatusAndIsActive(PostStatus postStatus, byte isActive);

    int countByIsActiveAndStatusAndTimeLessThanEqual(byte isActive,
                                                     PostStatus status,
                                                     ZonedDateTime time
    );

    int countByUserAndIsActiveAndStatusAndTimeLessThanEqual(User user, byte isActive,
                                                            PostStatus accepted, ZonedDateTime now);

    @Modifying
    @Query("UPDATE Post p SET p.viewCount = ?2 WHERE p.id = ?1")
    void updateViewCount(int id, int viewCount);

    @Query("SELECT SUM(p.viewCount) FROM Post p " +
            "WHERE p.isActive = 1 AND p.status='ACCEPTED' AND p.time <= ?1")
    int viewCountSum(ZonedDateTime dateTime);

    @Query(
            value = "SELECT DATE(time) AS day, COUNT(DISTINCT id)  AS post_num " +
                    "FROM posts " +
                    "WHERE time BETWEEN ?1 AND ?2 " +
                    "GROUP BY day",
            nativeQuery = true)
    List<String> findTotalPostsCount4EveryDay(LocalDateTime startDate, LocalDateTime endDate);






    @Query( "FROM Post p " +
            "WHERE p.isActive = 1 AND p.status='ACCEPTED' " +
            "AND p.time >= ?1 AND p.time <= ?2")
    Page<Post> findPostsByDate(ZonedDateTime dateStart,
                               ZonedDateTime dateFinish,
                               Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "WHERE p.isActive = 1 AND p.status='ACCEPTED'")
    List<Post> findActiveAndAcceptedPosts();

    @Query(
            value = "SELECT p.id," +
                    "    UNIX_TIMESTAMP(p.time) as timestamp," +
                    "    p.user_id as user_id," +
                    "(SELECT name FROM users" +
                    "         WHERE id = p.user_id) user_name," +
                    "    p.title," +
                    "    p.text as announce," +
                            "(SELECT COUNT(*) FROM post_votes" +
                     "         WHERE post_id = p.id AND value=1) likeCount," +
                    "(SELECT COUNT(*) FROM post_votes" +
                    "         WHERE post_id = p.id AND value=-1) dislikeCount," +
                    "   (SELECT COUNT(*) FROM post_comments" +
                    "         WHERE post_id = p.id) commentCount," +
                    "    p.view_count as viewCount FROM posts p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED'",
            nativeQuery = true)
    Map<String, String> findAllPosts();

}
