package store;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_MAX_PRICE_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_NAME_FORMAT_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_PRICE_UNIT_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_QUANTITY_RANGE_EXCEPTION;
import static store.exception.ProductsFileExceptionMessage.PRODUCTS_FILE_WRONG_PROMOTION_EXCEPTION;
import static store.view.FileLocation.PRODUCTS;
import static store.view.FileLocation.PROMOTIONS;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import store.exception.ProductsFileException;
import store.exception.PromotionsFileException;

class ConvenienceStoreTest extends NsTest {

    ConvenienceStore convenienceStore;

    @BeforeEach
    void setUp() {
        convenienceStore = new ConvenienceStore();
    }

    @Nested
    @DisplayName("요구사항 테스트")
    class requirementsTests {

        @Test
        @DisplayName("상품명과 수량 입력 안내 출력")
        void productNameAndQuantityGuide() {
            assertSimpleTest(() -> {
                run("[사이다-6]", "Y", "N");
                assertThat(output()).contains("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            });
        }

        @Test
        @DisplayName("프로모션 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부 입력(Y or N)")
        void lessThenPromotion() {
            assertSimpleTest(() -> {
                run("[사이다-2]", "N", "N", "N");
                assertThat(output()).contains("현재 사이다은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
            });
        }

        @Test
        @DisplayName("프로모션 수량보다 적게 가져온 경우, 그 수량만큼 추가 선택 시")
        void lessThenPromotionYes() {
            assertSimpleTest(() -> {
                run("[사이다-2]", "Y", "N", "N");
                assertThat(output().replaceAll("\\s","")).contains("사이다33,000");
            });
        }

        @Test
        @DisplayName("프로모션 재고가 부족하여 일부 수량 프로모션 혜택 없이 결제해야 하는 경우")
        void lessPromotionStock() {
            assertSimpleTest(() -> {
                run("[사이다-12]", "N", "N", "N");
                assertThat(output()).contains("현재 사이다 3개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
            });
        }

        @Test
        @DisplayName("프로모션 재고가 부족하여 일부 수량 프로모션 혜택 없이 결제해야 하는 경우")
        void lessPromotionStockNo() {
            assertSimpleTest(() -> {
                run("[사이다-12]", "N", "N", "N");
                assertThat(output().replaceAll("\\s","")).contains("사이다99,000");
            });
        }

        @Test
        @DisplayName("멤버십 할인 적용 여부 입력 안내 문구 출력")
        void membershipDiscount() {
            assertSimpleTest(() -> {
                run("[사이다-12]", "N", "N", "N");
                assertThat(output()).contains("멤버십 할인을 받으시겠습니까? (Y/N)");
            });
        }

        @Test
        @DisplayName("멤버십 할인 적용 시")
        void membershipDiscountYes() {
            assertSimpleTest(() -> {
                run("[초코바-8]", "Y", "Y", "N");
                assertThat(output().replaceAll("\\s","")).contains("멤버십할인-720");
            });
        }

        @Test
        @DisplayName("추가 구매 여부 확인 안내 문구 출력")
        void checkAdditionalPurchase() {
            assertSimpleTest(() -> {
                run("[사이다-6]", "Y", "N");
                assertThat(output()).contains("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
            });
        }

        @Test
        @DisplayName("추가 구매 선택 시")
        void checkAdditionalPurchaseYes() {
            assertSimpleTest(() -> {
                run("[사이다-6]", "Y", "Y", "[콜라-3]", "Y", "N");
                assertThat(output().replaceAll("\\s","")).contains("-사이다1,000원2개탄산2+1");
            });
        }

        @Test
        @DisplayName("멤버십 할인 최대 한도는 8,000원")
        void maxDiscountTest() {
            assertSimpleTest(() -> {
                run("[정식도시락-8]", "Y", "N");
                assertThat(output().replaceAll("\\s","")).contains("멤버십할인-8,000");
            });
        }

        @Test
        @DisplayName("영수증 테스트")
        void eventDiscount() {
            assertSimpleTest(() -> {
                run("[콜라-3],[오렌지주스-2],[에너지바-5]", "Y", "N");
                assertThat(output()).contains("""
                        ===========W 편의점=============
                        상품명		수량	금액
                        콜라		3 	3,000
                        오렌지주스		2 	3,600
                        에너지바		5 	10,000
                        ===========증	정=============
                        콜라		1
                        오렌지주스		1
                        ==============================
                        총구매액		10		16,600
                        행사할인				-2,800
                        멤버십할인				-3,000
                        내실돈				10,800
                        """);
            });
        }
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
            @DisplayName("name,price,quantity,promotion 형식으로 된 파일을 등록해주세요")
            void formatException() {
                assertSimpleTest(() ->
                        assertThatThrownBy(() -> convenienceStore.setUpProductsFile(
                                "src/main/resources/productsException/lesserElementsFormatException.md"))
                                .isInstanceOf(IllegalArgumentException.class)
                                .isInstanceOf(ProductsFileException.class)
                                .hasMessageContaining("[ERROR] 상품 목록 파일의 형식이 잘못되었습니다. name,price,quantity,promotion 형식으로 된 파일을 등록해주세요. (예: 탄산수,1200,5,탄산2+1)"));
            }

            @Test
            @DisplayName("이름은 한글 형식으로 적어주세요")
            void nameException() {
                assertThatThrownBy(() -> convenienceStore.setUpProductsFile(
                        "src/main/resources/productsException/nameException.md"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .isInstanceOf(ProductsFileException.class)
                        .hasMessageContaining("[ERROR] 상품 목록 파일의 형식이 잘못되었습니다. " + PRODUCTS_FILE_NAME_FORMAT_EXCEPTION);
            }

            @Test
            @DisplayName("1,000,000,000원 미만의 가격을 적어주세요")
            void maxCostException() {
                assertThatThrownBy(() -> convenienceStore.setUpProductsFile(
                        "src/main/resources/productsException/maxCostException.md"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .isInstanceOf(ProductsFileException.class)
                        .hasMessageContaining("[ERROR] 상품 목록 파일의 형식이 잘못되었습니다. " + PRODUCTS_FILE_MAX_PRICE_EXCEPTION);
            }

            @Test
            @DisplayName("가격은 100원 단위로 적어주세요")
            void costUnitException() {
                assertThatThrownBy(() -> convenienceStore.setUpProductsFile(
                        "src/main/resources/productsException/costUnitException.md"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .isInstanceOf(ProductsFileException.class)
                        .hasMessageContaining("[ERROR] 상품 목록 파일의 형식이 잘못되었습니다. " + PRODUCTS_FILE_PRICE_UNIT_EXCEPTION);
            }

            @Test
            @DisplayName("재고는 1,000개 미만으로 등록해주세요")
            void maxQuantityException() {
                assertThatThrownBy(() -> convenienceStore.setUpProductsFile(
                        "src/main/resources/productsException/maxQuantityException.md"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .isInstanceOf(ProductsFileException.class)
                        .hasMessageContaining("[ERROR] 상품 목록 파일의 형식이 잘못되었습니다. " + PRODUCTS_FILE_QUANTITY_RANGE_EXCEPTION);
            }

            @Test
            @DisplayName("현재 운영되고 있는 프로모션 중 해당하는 프로모션이 없습니다")
            void promotionException() {
                assertThatThrownBy(() -> convenienceStore.setUpProductsFile(
                        "src/main/resources/productsException/promotionException.md"))
                        .isInstanceOf(IllegalArgumentException.class)
                        .isInstanceOf(ProductsFileException.class)
                        .hasMessageContaining("[ERROR] 상품 목록 파일의 형식이 잘못되었습니다. " + PRODUCTS_FILE_WRONG_PROMOTION_EXCEPTION);
            }
        }
    }

    @Override
    protected void runMain() {
        convenienceStore.run(PROMOTIONS.getLocation(), PRODUCTS.getLocation());
    }
}