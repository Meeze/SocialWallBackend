package de.udg.socialwall.model.facebook;

import lombok.Data;

@Data
public class FacebookResponse {

    private FacebookPost[] data;
    private Object paging;

}
