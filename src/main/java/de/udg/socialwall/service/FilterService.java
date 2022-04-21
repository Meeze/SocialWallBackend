package de.udg.socialwall.service;

import de.udg.socialwall.model.generic.SocialMediaPost;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Getter
public class FilterService {

    public Set<SocialMediaPost> applyFilters(Set<SocialMediaPost> posts, String... filters) {
        for (String filter: filters) {
            posts = filter(posts, filter);
        }
        return posts;
    }

    private Set<SocialMediaPost> filter(Set<SocialMediaPost> posts, String filter) {
        return posts.stream().filter(socialMediaPost -> socialMediaPost.getText().contains(filter)).collect(Collectors.toSet());
    }

}
