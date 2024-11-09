package store.util;

public class ValidationUtil {

    public static <T extends IllegalArgumentException> void validateNull(String input, T exception) {
        if (input == null || input.isBlank()) {
            throw exception;
        }
    }

    public static <T extends IllegalArgumentException> void validateLong(String input, T exception) {
        validateNull(input, exception);
        try {
            Long.parseLong(input);
        } catch (NumberFormatException e) {
            throw exception;
        }
    }

    public static <T extends IllegalArgumentException>
    void validateFormatHangul(String rawName, T exception) {
        validateNull(rawName, exception);
        if (!rawName.matches("\\p{IsHangul}+")) {
            throw exception;
        }
    }
}
