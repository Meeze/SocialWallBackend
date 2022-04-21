package de.udg.socialwall.service;

import de.udg.socialwall.model.generic.CropType;
import de.udg.socialwall.model.generic.SocialMediaPost;
import de.udg.socialwall.model.facebook.FacebookPost;
import de.udg.socialwall.model.facebook.FacebookResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter(AccessLevel.PROTECTED)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FacebookService extends AbstractApiService {

    private final PostConversionService conversionService;
    private final RestTemplate restTemplate;

    @Value("${accesstoken.facebook}")
    private String accessToken;
    private String fields = "id,message,permalink_url,attachments,shares,link,from";
    private String requestParams = "?fields=" + fields + "&access_token=";
    private String graph = "https://graph.facebook.com/v13.0/";
    private String mediaEndpoint = "me/posts";

    private String requestUrl = graph + mediaEndpoint + requestParams;

    public List<SocialMediaPost> getPosts() {
        FacebookPost[] mediaIds = getMediaList();
        List<SocialMediaPost> posts = Arrays.stream(mediaIds).map(getConversionService()::fromFacebook).collect(Collectors.toList());
        return posts;
    }

    private FacebookPost[] getMediaList() {
        ResponseEntity<FacebookResponse> response = request(requestUrl + getAccessToken(), FacebookResponse.class);
        return Objects.requireNonNull(response.getBody()).getData();
    }



}
