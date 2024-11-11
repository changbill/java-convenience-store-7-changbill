package store.exception;

public class InputException extends IllegalArgumentException {
    public InputException(String message) {
        super("[ERROR] " + message + " 다시 입력해 주세요.");
    }
}
