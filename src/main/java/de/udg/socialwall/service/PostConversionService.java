package de.udg.socialwall.service;

import de.udg.socialwall.model.generic.Platform;
import de.udg.socialwall.model.generic.SocialMediaImage;
import de.udg.socialwall.model.generic.SocialMediaPost;
import de.udg.socialwall.model.facebook.FacebookPost;
import de.udg.socialwall.model.insta.InstagramPost;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class PostConversionService {

    private SocialMediaImage wrapAsSocialImage(String url, String alt) {
        return SocialMediaImage.builder().url(url).alt(alt).build();
    }

    public SocialMediaPost fromInsta(InstagramPost instagramPost) {
        SocialMediaPost post = SocialMediaPost.builder().
                url(instagramPost.getPermalink()).
                image(wrapAsSocialImage(instagramPost.getMedia_url(), "Some Instagram Image!")).
                text(instagramPost.getCaption()).
                impressions(0).
                accountName(instagramPost.getUsername()).
                platform(Platform.INSTAGRAM).build();
        return post;
    }

    public SocialMediaPost fromFacebook(FacebookPost facebookPost) {
        int impressions = facebookPost.getShares() != null ? facebookPost.getShares().getCount() : 0;
        String imageUrl = facebookPost.getAttachments() != null ? facebookPost.getAttachments().getData()[0].getMedia().getImage().getSrc() : "";
        SocialMediaPost post = SocialMediaPost.builder().
                url(facebookPost.getPermalink_url()).
                image(wrapAsSocialImage(imageUrl, "Some Facebook Image!")).
                text(facebookPost.getMessage()).
                impressions(impressions).
                accountName(facebookPost.getFrom().getName()).
                platform(Platform.FACEBOOK).build();
        return post;
    }

}
