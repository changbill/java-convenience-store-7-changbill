package store.view;

import java.util.List;

public class OutputView {
    private static final String GREET_MESSAGE = "안녕하세요. W편의점입니다.";
    private static final String STOCK_INTRODUCTION_MESSAGE = "현재 보유하고 있는 상품입니다.";

    public void printIntroductionMessage(List<String> messages) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GREET_MESSAGE).append("\n")
                                .append(STOCK_INTRODUCTION_MESSAGE).append("\n")
                .append("\n");
        for (String message : messages) {
            stringBuilder.append(message).append("\n");
        }
        System.out.println(stringBuilder);
    }
}
