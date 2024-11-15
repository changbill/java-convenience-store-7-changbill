package store.service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import store.model.dto.ProductOrderDto;
import store.model.ProductInformation;
import store.model.dto.ReceiptDto;
import store.model.dto.orderCalculationResponse.OrderCalculationResponse;
import store.model.setup.PromotionInformation;
import store.repository.ProductInformationRepository;
import store.service.parse.InputParser;
import store.service.parse.OutputParser;
import store.service.parse.ProductsParser;
import store.service.parse.PromotionsParser;
import store.util.ParseUtil;
import store.model.dto.ProductDto;

public class StoreService {
    private static final int HEADER_INDEX = 0;

    private final ProductInformationRepository productInformationRepository;
    private final InputParser inputParser;
    private final OutputParser outputParser;
    private final ProductsParser productsParser;
    private final PromotionsParser promotionsParser;

    public StoreService(
            ProductInformationRepository productInformationRepository,
            InputParser inputParser,
            OutputParser outputParser,
            ProductsParser productsParser,
            PromotionsParser promotionsParser
    ) {
        this.productInformationRepository = productInformationRepository;
        this.inputParser = inputParser;
        this.outputParser = outputParser;
        this.productsParser = productsParser;
        this.promotionsParser = promotionsParser;
    }

    public List<PromotionInformation> savePromotionInformation(List<String> rawPromotionInformations) {
        rawPromotionInformations.remove(HEADER_INDEX);

        return rawPromotionInformations.stream()
                .map(rawPromotionInformation ->
                                promotionsParser.parseToPromotionInformation(
                                        ParseUtil.splitByComma(rawPromotionInformation)))
                .filter(Objects::nonNull)
                .toList();
    }

    public List<ProductInformation> saveProductInformation(List<String> rawProductInformations) {
        rawProductInformations.remove(HEADER_INDEX);

        return rawProductInformations.stream().map(
                        rawProductInformation ->
                                productsParser.parseToProductInformation(
                                        ParseUtil.splitByComma(rawProductInformation))
                )
                .toList();
    }

    public List<String> getProductsToString() {
        List<ProductDto> productDtos = productInformationRepository.findAllProducts();
        return outputParser.parseProductsToString(productDtos);
    }

    public List<OrderCalculationResponse> getCalculationResponse(String rawProductOrder) {
        List<ProductOrderDto> productOrderDtos = inputParser.parseToProductOrderDto(rawProductOrder);
        List<OrderCalculationResponse> orderCalculationResponses =
                productInformationRepository.calculateProductOrders(productOrderDtos);

        orderCalculationResponses.sort(Comparator.comparing(OrderCalculationResponse::getResponseMessage));
        return orderCalculationResponses;
    }

    public List<ReceiptDto> putOrders(List<ProductOrderDto> orders) {
        List<ReceiptDto> receiptDtos = productInformationRepository.calculatePrice(orders);
        productInformationRepository.putProductOrders(orders);
        return receiptDtos;
    }
}
