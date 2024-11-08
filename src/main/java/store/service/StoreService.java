package store.service;

import java.util.List;
import store.model.administrator.ProductInformation;
import store.model.administrator.PromotionInformation;
import store.repository.StoreRepository;
import store.service.parse.OutputParser;
import store.service.parse.ProductsParser;
import store.service.parse.PromotionsParser;
import store.util.ParseUtil;
import store.util.ProductDto;

public class StoreService {
    private static final int HEADER_INDEX = 0;

    private final StoreRepository storeRepository;
    private final OutputParser outputParser;
    private final ProductsParser productsParser;
    private final PromotionsParser promotionsParser;

    public StoreService(
            StoreRepository storeRepository,
            OutputParser outputParser,
            ProductsParser productsParser,
            PromotionsParser promotionsParser
    ) {
        this.storeRepository = storeRepository;
        this.outputParser = outputParser;
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

    public List<String> getProductsToString() {
        List<ProductDto> productDtos = storeRepository.findAllProducts();
        return outputParser.parseProductsToString(productDtos);
    }

//    public void getPurchaseResult(String purchaseContext) {
//        storeRepository. ((purchaseContext));
//    }
}
