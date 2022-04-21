package de.udg.socialwall.model.facebook;

import lombok.Data;

@Data
public class FacebookPost {

    private String id;
    private String message = "";
    private FacebookPoster from;
    private String permalink_url;
    private Shares shares;
    private Attachments attachments;

}
