package de.udg.socialwall.model.generic;

import lombok.Data;

public enum CropType {
    ONETOONE("1:1"),
    TWOTOONE("2:1");

    private String ratio;

    CropType(String ratio) {
        this.ratio = ratio;
    }
}
