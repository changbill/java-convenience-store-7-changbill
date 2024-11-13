package store.util;

import java.util.Arrays;
import java.util.List;

import static store.util.ParseConstant.COMMA_DELIMITER;
import static store.util.ParseConstant.THOUSAND_UNIT_COMMA_FORMAT;

public class ParseUtil {

    public static String thousandDelimiter(long input) {
        return String.format(THOUSAND_UNIT_COMMA_FORMAT.getValue(), input);
    }

    public static long parseToLong(String input) {
        return Long.parseLong(input);
    }

    public static List<String> splitByComma(String text) {
        return Arrays.stream(text.split(COMMA_DELIMITER.getValue())).toList();
    }
}
