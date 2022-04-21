package de.udg.socialwall.model.insta;

import lombok.Data;

@Data
public class InstagramResponse {

    private InstagramPost[] data;
    private Object paging;

}
