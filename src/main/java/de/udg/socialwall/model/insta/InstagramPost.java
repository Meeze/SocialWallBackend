package de.udg.socialwall.model.insta;

import lombok.Data;

//see https://developers.facebook.com/docs/instagram-api/reference/ig-media for reference
@Data
public class InstagramPost {
    private String id;
    private String username;
    private String caption = "";
    private String media_url;
    private String permalink;
}
