package de.udg.socialwall.model.generic;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MergeOptions {
    private boolean onlyImages;
    private boolean noDuplicates;
}
