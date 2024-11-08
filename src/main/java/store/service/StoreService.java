package store.service;

import java.util.List;
import store.model.administrator.ProductInformation;
import store.model.administrator.PromotionInformation;
import store.repository.StoreRepository;
import store.service.parse.ProductsParser;
import store.service.parse.PromotionsParser;
import store.util.ParseUtil;

public class StoreService {
    private static final int HEADER_INDEX = 0;

    private final StoreRepository storeRepository;
    private final ProductsParser productsParser;
    private final PromotionsParser promotionsParser;

    public StoreService(StoreRepository storeRepository, ProductsParser productsParser,
                        PromotionsParser promotionsParser) {
        this.storeRepository = storeRepository;
        this.productsParser = productsParser;
        this.promotionsParser = promotionsParser;
    }

    public List<PromotionInformation> savePromotionInformation(List<String> rawPromotionInformations) {
        // TODO: Header 형식 확인하는 예외 만들기
        rawPromotionInformations.remove(HEADER_INDEX);

        return rawPromotionInformations.stream().map(
                        rawPromotionInformation ->
                                promotionsParser.parseToPromotionInformation(
                                        ParseUtil.splitByComma(rawPromotionInformation))
                )
                .toList();
    }

    public List<ProductInformation> saveProductInformation(List<String> rawProductInformations) {
        // TODO: Header 형식 확인하는 예외 만들기
        rawProductInformations.remove(HEADER_INDEX);

        return rawProductInformations.stream().map(
                        rawProductInformation ->
                                productsParser.parseToProductInformation(
                                        ParseUtil.splitByComma(rawProductInformation))
                )
                .toList();
    }

//    public void getPurchaseResult(String purchaseContext) {
//        storeRepository. ((purchaseContext));
//    }
}
