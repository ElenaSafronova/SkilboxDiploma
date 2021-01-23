package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.controller.ApiGeneralController;
import ru.skillbox.diploma.controller.ApiPostController;
import ru.skillbox.diploma.model.Tag;
import ru.skillbox.diploma.repository.PostRepository;
import ru.skillbox.diploma.repository.TagRepository;
import ru.skillbox.diploma.responce.TagResponse;
import ru.skillbox.diploma.value.PostStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    Logger logger = LoggerFactory.getLogger(ApiGeneralController.class);

    public Iterable<Tag> findAll() {
        return tagRepository.findAll();
    }

    public List<TagResponse> findTagsWithWeight(String tagQuery) {
        int postsTotalCount = (postRepository
                .findAllByIsActiveAndStatusAndTimeLessThanEqual(
                        (byte) 1,
                        PostStatus.ACCEPTED,
                        ZonedDateTime.now(),
                        PageRequest.of(0, Integer.MAX_VALUE)
                ))
                .toList().size(); // количество активных публикаций, утверждённых модератором со временем публикации, не превышающем текущее время

        Iterable<Tag> allTags;

        if (tagQuery.isBlank()){
            allTags = tagRepository.findAll();
        }
        else{
            logger.trace("?query=" + tagQuery);
            allTags = tagRepository.findByNameContaining(tagQuery);
        }

        System.out.println("PostsByTags table:");
        List<TagResponse> tagResponses = new ArrayList<>();
        allTags.forEach(curTag ->{
            int postsCountByTag = ((List) postRepository
                    .findByTagsContaining(curTag)).size();  //Кол-во публикаций с данным тэгом
            System.out.println("curTag " + curTag.getName()
                    + " postsCountByTag " + postsCountByTag);

            double weight = (double) postsCountByTag / postsTotalCount;
            tagResponses.add(new TagResponse(curTag.getName(), weight));
        });

        return tagResponses;
    }
}
