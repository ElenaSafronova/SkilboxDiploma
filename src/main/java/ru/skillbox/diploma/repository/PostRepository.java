package ru.skillbox.diploma.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diploma.model.Post;
import ru.skillbox.diploma.value.PostStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
    List<Post> findAllByIsActiveAndStatusAndTimeLessThanEqual(byte isActive, PostStatus status, ZonedDateTime time, Pageable pageable);

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

//    STR_TO_DATE(time, '%Y-%m-%d')

//    @Query("SELECT p FROM posts p WHERE p.text LIKE %?1%")
//    List<Post> search(String keyword);
}
