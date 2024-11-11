package store;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static store.view.FileLocation.PROMOTIONS;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PromotionDuplicateTest extends NsTest {

    @Test
    @DisplayName("프로모션이 겹칠 경우 할인율 높은 순으로 우선 적용")
    void applyHigherDiscountPromotionFirst() {
        assertSimpleTest(() -> {
            run("[콜라-6]", "Y", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 MD추천상품",
                    "- 콜라 1,000원 20개",
                    "===========증\t정=============\n"
                            + "콜라\t\t3\n"
                            + "==============================\n"
                            + "총구매액\t\t6\t\t6,000\n"
                            + "행사할인\t\t\t\t-3,000\n"
                            + "멤버십할인\t\t\t\t0\n"
                            + "내실돈\t\t\t\t3,000"
                    );
        });
    }


    @Override
    protected void runMain() {
        ConvenienceStore convenienceStore = new ConvenienceStore();
        convenienceStore.run(PROMOTIONS.getLocation(), "src/main/resources/promotionDuplicateException.md");
    }
}
