package de.udg.socialwall.model.generic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialMediaPost {
    private Platform platform;
    private String accountName;
    private String url;
    private String text;
    private SocialMediaImage image;
    private int impressions;
}
