package store.repository;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import store.model.setup.PromotionInformation;

class PromotionInformationRepositoryTest {

    PromotionInformationRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PromotionInformationRepository();
    }

    @Test
    void 프로모션이_겹칠_경우_할인율_높은순으로_우선_적용_새_프로모션이_높은_경우() {
        assertSimpleTest(() -> {
                    PromotionInformation existingPromotion = PromotionInformation.of(
                            "promotion",
                            2,
                            1,
                            LocalDate.of(2024, 11, 1).atStartOfDay(),
                            LocalDate.of(2024, 12, 1).atStartOfDay()
                    );

                    PromotionInformation newPromotion = PromotionInformation.of(
                            "promotion",
                            1,
                            1,
                            LocalDate.of(2024, 11, 1).atStartOfDay(),
                            LocalDate.of(2024, 12, 1).atStartOfDay()
                    );

                    repository.addPromotionInformation("promotion", existingPromotion);
                    repository.addPromotionInformation("promotion", newPromotion);

                    assertEquals(repository.findPromotionInformation("promotion"), newPromotion);
                }
        );
    }

    @Test
    void 프로모션이_겹칠_경우_할인율_높은순으로_우선_적용_기존_프로모션이_높은_경우() {
        assertSimpleTest(() -> {
                    PromotionInformation existingPromotion = PromotionInformation.of(
                            "promotion",
                            1,
                            1,
                            LocalDate.of(2024, 11, 1).atStartOfDay(),
                            LocalDate.of(2024, 12, 1).atStartOfDay()
                    );

                    PromotionInformation newPromotion = PromotionInformation.of(
                            "promotion",
                            2,
                            1,
                            LocalDate.of(2024, 11, 1).atStartOfDay(),
                            LocalDate.of(2024, 12, 1).atStartOfDay()
                    );

                    repository.addPromotionInformation("promotion", existingPromotion);
                    repository.addPromotionInformation("promotion", newPromotion);

                    assertEquals(repository.findPromotionInformation("promotion"), existingPromotion);
                }
        );
    }
}