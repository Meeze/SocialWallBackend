package de.udg.socialwall.model.generic;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class StringFilter {

    private String[] filter = new String[0];

    public static StringFilter create(String... strings) {
        switch (strings.length) {
            case 0:
                return new StringFilter();
            case 1:
                return new StringFilter(makeList(strings[0]));
            default:
                return new StringFilter(strings);
        }

    }

    private static String[] makeList(String s) {
        String[] arr = new String[1];
        arr[0] = s;
        return arr;
    }

    public boolean isUnfiltered() {
        if (filter.length < 2) {
            if (filter.length < 1) {
                return true;
            } else {
                return !Objects.equals(filter[0], "");
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.asList(filter).hashCode();
    }
}
