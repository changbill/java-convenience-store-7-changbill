package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.model.dto.orderCalculationResponse.OrderCalculationResponse;
import store.model.dto.orderCalculationResponse.SomeDontBenefitResponse;
import store.model.dto.orderCalculationResponse.TakeExtraBenefitResponse;

public class InputView {
    private static final String PRODUCT_INPUT_INTRODUCTION_MESSAGE = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String DO_YOU_HAVE_MEMBERSHIP_DISCOUNT = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String DO_YOU_NEED_SOMETHING_ELSE = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String getDoYouNeedSomethingElse() {
        System.out.println(DO_YOU_NEED_SOMETHING_ELSE);
        return Console.readLine();
    }

    public String questionMembershipDiscount() {
        System.out.println(DO_YOU_HAVE_MEMBERSHIP_DISCOUNT);
        return Console.readLine();
    }

    public String printPurchaseResponse(OrderCalculationResponse orderCalculationResponse) {
        if (orderCalculationResponse.getClass() == TakeExtraBenefitResponse.class) {
            System.out.printf(
                    (orderCalculationResponse.getResponseMessage().getMessage()) + "\n",
                    orderCalculationResponse.getProductOrderDto().productName(),
                    ((TakeExtraBenefitResponse) orderCalculationResponse).getBenefitQuantity()
            );
        } else if (orderCalculationResponse.getClass() == SomeDontBenefitResponse.class) {
            System.out.printf(
                    (orderCalculationResponse.getResponseMessage().getMessage()) + "\n",
                    orderCalculationResponse.getProductOrderDto().productName(),
                    ((SomeDontBenefitResponse) orderCalculationResponse).getNotBenefitQuantity()
            );
        }

        return Console.readLine();
    }

    public String purchaseInput() {
        System.out.println(PRODUCT_INPUT_INTRODUCTION_MESSAGE);
        return Console.readLine();
    }

    public List<String> readFile(String fileLocation) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileLocation));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }

            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return lines;
    }
}
