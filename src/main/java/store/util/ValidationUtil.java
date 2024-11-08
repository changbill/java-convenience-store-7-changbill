package store.util;

public class ValidationUtil {

    public static <T extends IllegalArgumentException> void validateNull(String input, T exception) {
        if(input == null || input.isBlank()) {
            throw exception;
        }
    }

    public static <T extends IllegalArgumentException> void validateLong(String input, T exception) {
        try {
            Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw exception;
        }
    }
}
