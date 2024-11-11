package store.view;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InputViewTest {

    private InputView inputView;

    @BeforeEach
    public void setUp() {
        inputView = new InputView();
    }

    @Test
    void products_파일_출력_테스트() {
        assertSimpleTest(() -> {
            //given
            List<String> fileInput = inputView.readFile(FileLocation.PRODUCTS.getLocation());
            String context = """
                    name,price,quantity,promotion
                    콜라,1000,10,탄산2+1
                    콜라,1000,10,null
                    사이다,1000,8,탄산2+1
                    사이다,1000,7,null
                    오렌지주스,1800,9,MD추천상품
                    탄산수,1200,5,탄산2+1
                    물,500,10,null
                    비타민워터,1500,6,null
                    감자칩,1500,5,반짝할인
                    감자칩,1500,5,null
                    초코바,1200,5,MD추천상품
                    초코바,1200,5,null
                    에너지바,2000,5,null
                    정식도시락,6400,8,null
                    컵라면,1700,1,MD추천상품
                    컵라면,1700,10,null
                    """;

            //when
            List<String> productContexts = Arrays.stream(context.split("\n")).toList();

            //then
            assertEquals(productContexts, fileInput);
        });

    }

    @Test
    void promotions_파일_출력_테스트() {
        assertSimpleTest(() -> {
            //given
            List<String> fileInput = inputView.readFile(FileLocation.PROMOTIONS.getLocation());
            String context = """
                    name,buy,get,start_date,end_date
                    탄산2+1,2,1,2024-01-01,2024-12-31
                    MD추천상품,1,1,2024-01-01,2024-12-31
                    반짝할인,1,1,2024-11-01,2024-11-30
                    """;

            //when
            List<String> promotionContexts = Arrays.stream(context.split("\n")).toList();

            //then
            assertEquals(promotionContexts, fileInput);
        });

    }
}
