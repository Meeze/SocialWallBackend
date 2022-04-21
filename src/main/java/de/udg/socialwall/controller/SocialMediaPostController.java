package de.udg.socialwall.controller;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.udg.socialwall.model.generic.StringFilter;
import de.udg.socialwall.model.generic.MergeOptions;
import de.udg.socialwall.model.generic.SocialMediaPost;
import de.udg.socialwall.service.FacebookService;
import de.udg.socialwall.service.FilterService;
import de.udg.socialwall.service.InstagramService;
import de.udg.socialwall.service.PostMergeService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SocialMediaPostController {

    private final InstagramService instagramService;
    private final FacebookService facebookService;
    private final PostMergeService postMergeService;
    private final FilterService filterService;

    private MergeOptions mergeOptions = MergeOptions.builder().onlyImages(true).noDuplicates(true).build();

    LoadingCache<StringFilter, Set<SocialMediaPost>> cache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(12, TimeUnit.HOURS)
            .build(
                    new CacheLoader<StringFilter, Set<SocialMediaPost>>() {
                        @Override
                        public Set<SocialMediaPost> load(StringFilter filter) throws Exception {
                            // loading unfiltered content
                            if(filter.isUnfiltered()) {
                                Set<SocialMediaPost> posts = postMergeService.mergeWithOptions(mergeOptions, instagramService.getPosts(), facebookService.getPosts());
                                return posts;
                            } else {
                                //when requesting a filter we get unfiltered content, apply filter and cache
                                Set<SocialMediaPost> unfilteredPosts = cache.get(filter);
                                Set<SocialMediaPost> filteredPosts = filterService.applyFilters(unfilteredPosts, filter.getFilter());
                                return filteredPosts;
                            }
                        }
                    });

    @SneakyThrows
    @GetMapping("/all")
    public Set<SocialMediaPost> getAll(@RequestParam String filter) {
        StringFilter stringFilter = StringFilter.create(filter);
        return cache.get(stringFilter);
    }


    @GetMapping("/insta")
    public List<SocialMediaPost> getInsta() {
        List<SocialMediaPost> posts = instagramService.getPosts();
        return posts;
    }

    @GetMapping("/fb")
    public List<SocialMediaPost> getAllFb() {
        List<SocialMediaPost> posts = facebookService.getPosts();
        return posts;
    }

}
