package store.model;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.InputExceptionMessage.INPUT_MORE_THEN_STOCK_EXCEPTION;
import static store.util.ResponseMessage.SOME_DONT_BENEFIT;
import static store.util.ResponseMessage.SUCCESS;
import static store.util.ResponseMessage.TAKE_EXTRA_BENEFIT;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.exception.InputException;
import store.model.dto.ProductOrderDto;
import store.model.setup.PromotionInformation;
import store.model.stock.GeneralStock;
import store.model.stock.PromotionStock;

class OrderCalculationTest {
    private static final long EXAMPLE_PRICE = 1000;
    private static final String EXAMPLE_PRODUCT_NAME = "사이다";
    private static final PromotionInformation ONEPLUSONE_PROMOTION =
            PromotionInformation.of(
                    "반짝할인",
                    1,
                    1,
                    LocalDate.of(2024, 2, 1).atStartOfDay(),
                    LocalDate.of(2024, 12, 1).atStartOfDay()
            );
    private static final PromotionInformation TWOPLUSONE_PROMOTION =
            PromotionInformation.of(
                    "탄산2+1",
                    2,
                    1,
                    LocalDate.of(2024, 2, 1).atStartOfDay(),
                    LocalDate.of(2024, 12, 1).atStartOfDay()
            );

    private ProductInformation productInformation;
    private ProductOrderDto productOrderDto;

    void stockSetup(
            long promotionStockQuantity,
            long generalStockQuantity,
            PromotionInformation promotion
    ) {
        productInformation = new ProductInformation(EXAMPLE_PRICE, GeneralStock.of(generalStockQuantity));
        productInformation.addProductInformation(PromotionStock.of(promotionStockQuantity, promotion));
    }

    void orderSetup(long quantity) {
        productOrderDto = ProductOrderDto.of(EXAMPLE_PRODUCT_NAME, quantity);
    }

    @Test
    void 재고부족_예외테스트() {
        assertSimpleTest(() -> {
            stockSetup(3, 3, ONEPLUSONE_PROMOTION);
            orderSetup(7);

            OrderCalculation orderCalculation = OrderCalculation.of(productInformation, productOrderDto);

            assertThatThrownBy(orderCalculation::responseOrderCalculation)
                    .isInstanceOf(IllegalArgumentException.class)
                    .isInstanceOf(InputException.class)
                    .hasMessageContaining(INPUT_MORE_THEN_STOCK_EXCEPTION);
        });
    }

    @Nested
    @DisplayName("프로모션 재고 있을경우 테스트")
    class PromotionStockOrderCalculationTests {

        @Test
        void 재고부족_예외테스트() {
            assertSimpleTest(() -> {
                stockSetup(1, 0, ONEPLUSONE_PROMOTION);
                orderSetup(2);

                OrderCalculation orderCalculation = OrderCalculation.of(productInformation, productOrderDto);

                assertThatThrownBy(orderCalculation::responseOrderCalculation)
                        .isInstanceOf(IllegalArgumentException.class)
                        .isInstanceOf(InputException.class)
                        .hasMessageContaining(INPUT_MORE_THEN_STOCK_EXCEPTION);
            });
        }

        @ParameterizedTest
        @MethodSource("provideTakeExtraBenefitSetup")
        void TAKE_EXTRA_BENEFIT_테스트(
                long promotionStockQuantity,
                long generalStockQuantity,
                PromotionInformation promotion,
                long orderQuantity
        ) {
            assertSimpleTest(() -> {
                stockSetup(promotionStockQuantity, generalStockQuantity, promotion);
                orderSetup(orderQuantity);

                OrderCalculation orderCalculation = OrderCalculation.of(productInformation, productOrderDto);

                assertEquals(
                        TAKE_EXTRA_BENEFIT,
                        orderCalculation.responseOrderCalculation().getResponseMessage()
                );
            });
        }

        @ParameterizedTest
        @MethodSource("provideSomeDontBenefitSetup")
        void SOME_DONT_BENEFIT_테스트(
                long promotionStockQuantity,
                long generalStockQuantity,
                PromotionInformation promotion,
                long orderQuantity
        ) {
            assertSimpleTest(() -> {
                stockSetup(promotionStockQuantity, generalStockQuantity, promotion);
                orderSetup(orderQuantity);

                OrderCalculation orderCalculation = OrderCalculation.of(productInformation, productOrderDto);

                assertEquals(
                        SOME_DONT_BENEFIT,
                        orderCalculation.responseOrderCalculation().getResponseMessage()
                );
            });
        }

        @ParameterizedTest
        @MethodSource("provideSuccessSetup")
        void SUCCESS_테스트(
                long promotionStockQuantity,
                long generalStockQuantity,
                PromotionInformation promotion,
                long orderQuantity
        ) {
            assertSimpleTest(() -> {
                stockSetup(promotionStockQuantity, generalStockQuantity, promotion);
                orderSetup(orderQuantity);

                OrderCalculation orderCalculation = OrderCalculation.of(productInformation, productOrderDto);

                assertEquals(SUCCESS, orderCalculation.responseOrderCalculation().getResponseMessage());
            });
        }

        private static Stream<Arguments> provideTakeExtraBenefitSetup() {
            return Stream.of(
                    Arguments.of(10, 10, ONEPLUSONE_PROMOTION, 9),
                    Arguments.of(9, 10, ONEPLUSONE_PROMOTION, 9),
                    Arguments.of(7, 10, TWOPLUSONE_PROMOTION, 8),
                    Arguments.of(8, 10, TWOPLUSONE_PROMOTION, 8),
                    Arguments.of(9, 10, TWOPLUSONE_PROMOTION, 8)
            );
        }

        private static Stream<Arguments> provideSomeDontBenefitSetup() {
            return Stream.of(
                    Arguments.of(10, 10, ONEPLUSONE_PROMOTION, 12),
                    Arguments.of(9, 10, ONEPLUSONE_PROMOTION, 12),
                    Arguments.of(7, 10, TWOPLUSONE_PROMOTION, 12),
                    Arguments.of(8, 10, TWOPLUSONE_PROMOTION, 12),
                    Arguments.of(9, 10, TWOPLUSONE_PROMOTION, 12),
                    Arguments.of(1, 10, ONEPLUSONE_PROMOTION, 4),
                    Arguments.of(1, 10, ONEPLUSONE_PROMOTION, 5)
            );
        }

        private static Stream<Arguments> provideSuccessSetup() {
            return Stream.of(
                    Arguments.of(10, 10, ONEPLUSONE_PROMOTION, 10),
                    Arguments.of(9, 10, ONEPLUSONE_PROMOTION, 10),
                    Arguments.of(7, 10, TWOPLUSONE_PROMOTION, 9),
                    Arguments.of(8, 10, TWOPLUSONE_PROMOTION, 9),
                    Arguments.of(9, 10, TWOPLUSONE_PROMOTION, 9),
                    Arguments.of(1, 10, ONEPLUSONE_PROMOTION, 3),
                    Arguments.of(1, 10, ONEPLUSONE_PROMOTION, 2)
            );
        }
    }

}