package store.service.parse;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static store.exception.InputExceptionMessage.INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import store.exception.InputException;
import store.model.dto.ProductOrderDto;
import store.repository.ProductInformationRepository;
import store.service.validation.InputValidationService;

class InputParserTest {

    InputParser inputParser = new InputParser(new ProductInformationRepository(), new InputValidationService());

    @Test
    void 주문내용_변환_테스트() {
        assertSimpleTest(() -> {
            assertEquals(
                    List.of(
                            ProductOrderDto.of("사이다", 2),
                            ProductOrderDto.of("감자칩", 1)
                    ),
                    inputParser.parseToProductOrderDto("[사이다-2],[감자칩-1]")
            );

            assertEquals(
                    List.of(
                            ProductOrderDto.of("콜라", 10),
                            ProductOrderDto.of("치킨", 5)
                    ),
                    inputParser.parseToProductOrderDto("[콜라-10],[치킨-5]")
            );

            assertEquals(
                    List.of(
                            ProductOrderDto.of("나쵸", 3)
                    ),
                    inputParser.parseToProductOrderDto("[나쵸-3]")
            );
        });
    }

    @Nested
    @DisplayName("주문내용 변환 예외테스트")
    class InputParserExceptionTests {

        @Test
        void 빈값_예외테스트() {
            assertSimpleTest(() ->
                    assertThatThrownBy(() -> inputParser.parseToProductOrderDto(""))
                            .isInstanceOf(IllegalArgumentException.class)
                            .isInstanceOf(InputException.class)
                            .hasMessageContaining(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION));
        }

        @Test
        void 형식_예외테스트() {
            assertSimpleTest(() ->
                    assertThatThrownBy(() -> inputParser.parseToProductOrderDto("나쵸-5"))
                            .isInstanceOf(IllegalArgumentException.class)
                            .isInstanceOf(InputException.class)
                            .hasMessageContaining(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION));
        }

        @Test
        void 구분자_사이_빈값_예외테스트() {
            assertSimpleTest(() ->
                    assertThatThrownBy(() -> inputParser.parseToProductOrderDto("[나쵸-5],,[사이다-10]"))
                            .isInstanceOf(IllegalArgumentException.class)
                            .isInstanceOf(InputException.class)
                            .hasMessageContaining(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION));
        }

        @Test
        void 구분자_없음_예외테스트() {
            assertSimpleTest(() ->
                    assertThatThrownBy(() -> inputParser.parseToProductOrderDto("[나쵸-5][사이다-1]"))
                            .isInstanceOf(IllegalArgumentException.class)
                            .isInstanceOf(InputException.class)
                            .hasMessageContaining(INPUT_PRODUCT_NAME_AND_COUNT_EXCEPTION));
        }
    }
}