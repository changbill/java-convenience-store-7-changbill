package store.util;

import java.util.Arrays;
import java.util.List;

public class ParseUtil {

    public static long parseToLong(String input) {
        return Long.parseLong(input);
    }

    public static List<String> splitByComma(String text) {
        return Arrays.stream(text.split(",")).toList();
    }
}
