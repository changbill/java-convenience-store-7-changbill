package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputView {
    private static final String PRODUCT_INPUT_INTRODUCTION_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public String purchaseInput() {
        System.out.println(PRODUCT_INPUT_INTRODUCTION_MESSAGE);
        return Console.readLine();
    }

    public List<String> readFile(String fileLocation) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLocation));
            while(true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }

            br.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return lines;
    }
}
