package de.udg.socialwall.service;

import de.udg.socialwall.model.generic.SocialMediaPost;
import de.udg.socialwall.model.insta.InstagramPost;
import de.udg.socialwall.model.insta.InstagramResponse;
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
public class InstagramService extends AbstractApiService {

    private final PostConversionService conversionService;
    private final RestTemplate restTemplate;

    @Value("${accesstoken.instagram}")
    private String accessToken;
    //https://developers.facebook.com/docs/instagram-api/reference/ig-media
    private final String fields = "id,username,caption,media_url,permalink";
    private final String requestParams = "?fields=" + fields + "&access_token=";
    private final String graph = "https://graph.instagram.com/v13.0/";
    private final String mediaEndpoint = "me/media";

    private final String requestUrl = graph + mediaEndpoint + requestParams;

    public List<SocialMediaPost> getPosts() {
        InstagramPost[] mediaIds = getMediaList();
        List<SocialMediaPost> posts = Arrays.stream(mediaIds).map(getConversionService()::fromInsta).collect(Collectors.toList());
        return posts;
    }

    private InstagramPost[] getMediaList() {
        ResponseEntity<InstagramResponse> response = request(requestUrl + getAccessToken(), InstagramResponse.class);
        return Objects.requireNonNull(response.getBody()).getData();
    }

}
