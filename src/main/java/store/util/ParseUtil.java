package store.util;

import java.util.Arrays;
import java.util.List;

public class ParseUtil {

    public static String thousandDelimiter(long input) {
        return String.format("%,d", input);
    }

    public static long parseToLong(String input) {
        return Long.parseLong(input);
    }

    public static List<String> splitByComma(String text) {
        return Arrays.stream(text.split(",")).toList();
    }
}
