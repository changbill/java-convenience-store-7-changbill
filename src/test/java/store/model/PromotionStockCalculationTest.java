package store.model;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static store.exception.InputExceptionMessage.INPUT_MORE_THEN_STOCK_EXCEPTION;
import static store.util.ResponseMessage.SOME_DONT_BENEFIT;
import static store.util.ResponseMessage.SUCCESS;
import static store.util.ResponseMessage.TAKE_EXTRA_BENEFIT;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.exception.WrongInputException;
import store.model.dto.ProductOrderDto;
import store.model.setup.PromotionInformation;
import store.model.stock.GeneralStock;
import store.model.stock.PromotionStock;
import store.repository.ProductInformationRepository;

class PromotionStockCalculationTest {
    private ProductInformationRepository repository;
    private final long price = 1000;
    private final String productName = "사이다";
    private ProductInformation productInformation;
    private ProductOrderDto productOrderDto;
    private static final PromotionInformation onePlusOnePromotion =
            PromotionInformation.of("반짝할인", 1, 1, LocalDateTime.now(), LocalDateTime.now());
    private static final PromotionInformation twoPlusOnePromotion =
            PromotionInformation.of("탄산2+1", 2, 1, LocalDateTime.now(), LocalDateTime.now());

    void stockSetup(long promotionStockQuantity, long generalStockQuantity, PromotionInformation promotion) {
        productInformation = new ProductInformation(price, GeneralStock.of(generalStockQuantity));
        productInformation.addProductInformation(PromotionStock.of(promotionStockQuantity, promotion));
    }

    void orderSetup(long quantity) {
        productOrderDto = ProductOrderDto.of(productName, quantity);
    }

    @BeforeEach
    void setUp() {
        repository = new ProductInformationRepository();
    }

    @Test
    void 재고부족_예외테스트() {
        assertSimpleTest(() -> {
            stockSetup(3, 3, onePlusOnePromotion);
            orderSetup(7);

            PromotionStockCalculation promotionStockCalculation = PromotionStockCalculation.of(productInformation, productOrderDto);

            assertThatThrownBy(promotionStockCalculation::responseCalculation)
                    .isInstanceOf(IllegalArgumentException.class)
                    .isInstanceOf(WrongInputException.class)
                    .hasMessageContaining(INPUT_MORE_THEN_STOCK_EXCEPTION);
        });
    }

    // TODO: 일반재고만 있는 경우 테스트
//    @ParameterizedTest
//    @MethodSource("provideGeneralStockSetup")
//    void 일반재고만_있는경우_테스트(
//            long promotionStockQuantity,
//            long generalStockQuantity,
//            PromotionInformation promotion,
//            long orderQuantity
//    ) {
//        assertSimpleTest(() -> {
//            stockSetup(promotionStockQuantity, generalStockQuantity, promotion);
//            orderSetup(orderQuantity);
//
//            assertEquals(SUCCESS, repository.calculateProductOrders(List.of(productOrderDto)).get(0).getResponseMessage());
//        });
//    }

    private static Stream<Arguments> provideGeneralStockSetup() {
        return Stream.of(
                Arguments.of(0, 10, onePlusOnePromotion, 1),
                Arguments.of(0, 10, onePlusOnePromotion, 2),
                Arguments.of(0, 10, onePlusOnePromotion, 3),
                Arguments.of(0, 10, onePlusOnePromotion, 4)
        );
    }

    @Nested
    @DisplayName("프로모션 재고 있을경우 테스트")
    class PromotionStockPromotionStockCalculationTests {

        @Test
        void 재고부족_예외테스트() {
            assertSimpleTest(() -> {
                stockSetup(1, 0, onePlusOnePromotion);
                orderSetup(2);

                PromotionStockCalculation promotionStockCalculation = PromotionStockCalculation.of(productInformation, productOrderDto);

                assertThatThrownBy(promotionStockCalculation::responseCalculation)
                        .isInstanceOf(IllegalArgumentException.class)
                        .isInstanceOf(WrongInputException.class)
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

                PromotionStockCalculation promotionStockCalculation = PromotionStockCalculation.of(productInformation, productOrderDto);

                assertEquals(TAKE_EXTRA_BENEFIT, promotionStockCalculation.responseCalculation().getResponseMessage());
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

                PromotionStockCalculation promotionStockCalculation = PromotionStockCalculation.of(productInformation, productOrderDto);

                assertEquals(SOME_DONT_BENEFIT, promotionStockCalculation.responseCalculation().getResponseMessage());
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

                PromotionStockCalculation promotionStockCalculation = PromotionStockCalculation.of(productInformation, productOrderDto);

                assertEquals(SUCCESS, promotionStockCalculation.responseCalculation().getResponseMessage());
            });
        }

        private static Stream<Arguments> provideTakeExtraBenefitSetup() {
            return Stream.of(
                    Arguments.of(10, 10, onePlusOnePromotion, 9),
                    Arguments.of(9, 10, onePlusOnePromotion, 9),
                    Arguments.of(7, 10, twoPlusOnePromotion, 8),
                    Arguments.of(8, 10, twoPlusOnePromotion, 8),
                    Arguments.of(9, 10, twoPlusOnePromotion, 8)
            );
        }

        private static Stream<Arguments> provideSomeDontBenefitSetup() {
            return Stream.of(
                    Arguments.of(10, 10, onePlusOnePromotion, 12),
                    Arguments.of(9, 10, onePlusOnePromotion, 12),
                    Arguments.of(7, 10, twoPlusOnePromotion, 12),
                    Arguments.of(8, 10, twoPlusOnePromotion, 12),
                    Arguments.of(9, 10, twoPlusOnePromotion, 12),
                    Arguments.of(1, 10, onePlusOnePromotion, 4),
                    Arguments.of(1, 10, onePlusOnePromotion, 5)
            );
        }

        private static Stream<Arguments> provideSuccessSetup() {
            return Stream.of(
                    Arguments.of(10, 10, onePlusOnePromotion, 10),
                    Arguments.of(9, 10, onePlusOnePromotion, 10),
                    Arguments.of(7, 10, twoPlusOnePromotion, 9),
                    Arguments.of(8, 10, twoPlusOnePromotion, 9),
                    Arguments.of(9, 10, twoPlusOnePromotion, 9),
                    Arguments.of(1, 10, onePlusOnePromotion, 3),
                    Arguments.of(1, 10, onePlusOnePromotion, 2)
            );
        }
    }

}