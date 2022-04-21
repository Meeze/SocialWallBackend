package de.udg.socialwall.model.generic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SocialMediaImage {
    private String url;
    private String alt;
}
