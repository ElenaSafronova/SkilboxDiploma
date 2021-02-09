package ru.skillbox.diploma.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.skillbox.diploma.model.Tag;
import ru.skillbox.diploma.repository.PostRepository;
import ru.skillbox.diploma.repository.TagRepository;
import ru.skillbox.diploma.Dto.TagDto;
import ru.skillbox.diploma.value.PostStatus;

import java.time.ZonedDateTime;
import java.util.*;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PostRepository postRepository;

    Logger logger = LoggerFactory.getLogger(TagService.class);

    public Iterable<Tag> findAll() {
        return tagRepository.findAll();
    }

    public List<TagDto> findTagsWithWeight(String tagQuery) {
        int postsTotalCount = countActivePosts(); // количество активных публикаций, утверждённых модератором со временем публикации, не превышающем текущее время
        Iterable<Tag> allTags;
        if (tagQuery.isBlank()){
            allTags = tagRepository.findAll();
        }
        else{
            logger.trace("?query=" + tagQuery);
            allTags = tagRepository.findByNameContaining(tagQuery);
        }
//        logger.trace("PostsByTags count: " + ((Collection<Tag>) allTags).size() +
//                "\t postsTotalCount = " + postsTotalCount);

        return generateTagResponses(allTags, postsTotalCount);
    }

    private List<TagDto> generateTagResponses(Iterable<Tag> allTags, int postsTotalCount) {
        List<TagDto> tagResponses = new ArrayList<>();
        Map<String, Integer> tagsMap = new HashMap<>(); // для промежуточных рассчетов
        int maxFrequency = 1;
        String tagPopular = null;
        for (Tag curTag : allTags) {
            String curTagName = curTag.getName();

            int postsCountByTag = ((List) postRepository
                    .findByTagsContaining(curTag)).size();  //Кол-во публикаций с данным тэгом
            tagsMap.put(curTagName, postsCountByTag);
            if (maxFrequency < postsCountByTag){
                maxFrequency = postsCountByTag;
                tagPopular = curTag.getName();
            }
            double weight = (double) postsCountByTag / postsTotalCount;

//            logger.info("curTag " + curTagName
//                    + " postsCountByTag " + postsCountByTag + " weight " + weight);
            tagResponses.add(new TagDto(curTagName, weight));
        }
        calculateWeights(tagResponses, postsTotalCount, maxFrequency, tagPopular);
        return tagResponses;
    }

    private void calculateWeights(List<TagDto> tagResponses, int postsTotalCount, int maxFrequency, String tagPopular) {
        double weightMax = (double) maxFrequency / postsTotalCount;
        double k = 1 / weightMax;
//        logger.trace("weightMax = " + weightMax + "\tk = " + k);
        for (TagDto el : tagResponses) {
            el.setWeight(el.getWeight().doubleValue() * k);
//            logger.info(el.getName() + " " + el.getWeight());
        }
    }

    private int countActivePosts() {
        return (postRepository
                .findAllByIsActiveAndStatusAndTimeLessThanEqual(
                        (byte) 1,
                        PostStatus.ACCEPTED,
                        ZonedDateTime.now(),
                        PageRequest.of(0, Integer.MAX_VALUE)
                ))
                .toList().size();
    }
}
