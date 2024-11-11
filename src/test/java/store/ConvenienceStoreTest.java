package store;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.exception.PromotionsFileException;

class ConvenienceStoreTest extends NsTest {

    ConvenienceStore convenienceStore;

    @BeforeEach
    void setUp() {
        convenienceStore = new ConvenienceStore();
    }


    @Nested
    @DisplayName("통합 예외테스트")
    class ExceptionTests {

        @Nested
        @DisplayName("사용자 입력 예외테스트")
        class UserInputExceptionTests {

            @ParameterizedTest
            @ValueSource(strings = {"{컵라면-3}", "[asd-3]", "[컵라면--3]", "[[컵라면-3]]", "asd", "\n"})
            @NullSource
            @DisplayName("올바르지 않은 형식으로 입력했습니다")
            void formatException(String text) {
                assertSimpleTest(() -> {
                    runException(text);
                    assertThat(output()).contains("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
                });
            }

            @ParameterizedTest
            @ValueSource(strings = {"y", "n", "A", "1", "!", "\n"})
            @NullSource
            @DisplayName("잘못된 입력입니다")
            void yesOrNoException(String yesOrNo) {
                assertSimpleTest(() -> {
                    runException("[사이다-4]", yesOrNo);
                    assertThat(output()).contains("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
                });
            }

            @ParameterizedTest
            @ValueSource(strings = {"[펩시-1]", "[칠성사이다-1]", "[치킨-1]", "[ㅁㄴㅇ-1]"})
            @DisplayName("존재하지 않는 상품입니다")
            void productNameDoesNotExistException(String text) {
                assertSimpleTest(() -> {
                    runException(text);
                    assertThat(output()).contains("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
                });
            }

            @ParameterizedTest
            @ValueSource(strings = {"[사이다-101]", "[콜라-1000000000]"})
            @DisplayName("최대 입력치는 100 입니다")
            void maxOrderException(String text) {
                assertSimpleTest(() -> {
                    runException(text);
                    assertThat(output()).contains("[ERROR] 최대 입력치는 100 입니다. 다시 입력해 주세요.");
                });
            }

            @ParameterizedTest
            @ValueSource(strings = {"[사이다-16]", "[콜라-21]", "[오렌지주스-10]", "[물-11]"})
            @DisplayName("재고 수량을 초과하여 구매할 수 없습니다")
            void runOutOfStockException(String text) {
                assertSimpleTest(() -> {
                    runException(text);
                    assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
                });
            }
        }

        @Nested
        @DisplayName("프로모션 목록 파일 예외테스트")
        class ReadPromotionsFileExceptionTests {

            @Test
            @DisplayName("이미 같은 이름의 행사가 존재합니다")
            void nameException() {
                assertSimpleTest(() ->
                        assertThatThrownBy(() ->
                                convenienceStore.setUpPromotionsFile(
                                        "src/main/resources/promotionsException/nameDuplicateException.md"
                                ))
                                .isInstanceOf(IllegalArgumentException.class)
                                .isInstanceOf(PromotionsFileException.class)
                                .hasMessageContaining("[ERROR] 프로모션 목록 파일의 형식이 잘못되었습니다. 이미 같은 이름의 행사가 존재합니다."));
            }

            @ParameterizedTest
            @DisplayName("name,buy,get,start_date,end_date 형식으로 된 파일을 등록해주세요")
            @ValueSource(strings = {
                    "src/main/resources/promotionsException/elementsLessThenFiveFormatException.md",
                    "src/main/resources/promotionsException/emptyElementFormatException.md"
            })
            void formatException(String location) {
                assertSimpleTest(() ->
                        assertThatThrownBy(() ->
                                convenienceStore.setUpPromotionsFile(location))
                                .isInstanceOf(IllegalArgumentException.class)
                                .isInstanceOf(PromotionsFileException.class)
                                .hasMessageContaining(
                                        "[ERROR] 프로모션 목록 파일의 형식이 잘못되었습니다. name,buy,get,start_date,end_date 형식으로 된 파일을 등록해주세요. (예: 탄산2+1,2,1,2024-01-01,2024-12-31)"
                                ));
            }

            @ParameterizedTest
            @DisplayName("'{상품명}2+1', '{상품명}1+1', 'MD추천상품', '반짝할인'과 같은 행사명을 등록해주세요")
            @ValueSource(strings = {
                    "src/main/resources/promotionsException/nameFormatException.md",
                    "src/main/resources/promotionsException/nameFormatException2.md"
            })
            void nameFormatException(String location) {
                assertSimpleTest(() ->
                        assertThatThrownBy(() -> convenienceStore.setUpPromotionsFile(location))
                                .isInstanceOf(IllegalArgumentException.class)
                                .isInstanceOf(PromotionsFileException.class)
                                .hasMessageContaining(
                                        "[ERROR] 프로모션 목록 파일의 형식이 잘못되었습니다. '{상품명}2+1', '{상품명}1+1', 'MD추천상품', '반짝할인'과 같은 행사명을 등록해주세요."));
            }

            @Test
            @DisplayName("buy와 get을 올바르게 등록해주세요")
            void buyGetException() {
                assertSimpleTest(() ->
                        assertThatThrownBy(() -> convenienceStore.setUpPromotionsFile(
                                "src/main/resources/promotionsException/buyGetException.md"))
                                .isInstanceOf(IllegalArgumentException.class)
                                .isInstanceOf(PromotionsFileException.class)
                                .hasMessageContaining("[ERROR] 프로모션 목록 파일의 형식이 잘못되었습니다. buy와 get을 올바르게 등록해주세요."));
            }

            @Test
            @DisplayName("프로모션 날짜를 올바르게 등록해주세요.")
            void dateFormatException() {
                assertSimpleTest(() ->
                        assertThatThrownBy(() -> convenienceStore.setUpPromotionsFile(
                                "src/main/resources/promotionsException/dateFormatException.md"))
                                .isInstanceOf(IllegalArgumentException.class)
                                .isInstanceOf(PromotionsFileException.class)
                                .hasMessageContaining("[ERROR] 프로모션 목록 파일의 형식이 잘못되었습니다. 프로모션 날짜를 올바르게 등록해주세요."));
            }
        }

        @Nested
        @DisplayName("상품 목록 파일 예외테스트")
        class ReadProductsFileExceptionTests {

            @Test
            @DisplayName("이름은 한글 형식으로 적어주세요")
            void nameException2() {
            }

            @Test
            @DisplayName("1,000,000,000원 미만의 가격을 적어주세요")
            void costException1() {
            }

            @Test
            @DisplayName("가격은 100원 단위로 적어주세요")
            void costException2() {
            }

            @Test
            @DisplayName("해당하는 프로모션이 없습니다")
            void promotionException() {
            }

            @Test
            @DisplayName("name,price,quantity,promotion 형식으로 된 파일을 등록해주세요")
            void fileFormatException() {
            }
        }

    }

    @Override
    protected void runMain() {
        convenienceStore.run();
    }
}