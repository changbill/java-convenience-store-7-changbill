package store.util;

public enum ValidationRegexConstant {
    HANGUL_AND_TWOPLUSONE("\\p{IsHangul}+2\\+1"),
    HANGUL_AND_ONEPLUSONE("\\p{IsHangul}+1\\+1"),
    HANGUL("\\p{IsHangul}+"),
    HANGUL_AND_NUMBER("\\[(\\p{IsHangul}+)-(\\d+)]");

    private final String regex;

    ValidationRegexConstant(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }
}
