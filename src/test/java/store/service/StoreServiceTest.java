package store.service;

import org.junit.jupiter.api.BeforeEach;
import store.repository.ProductInformationRepository;
import store.repository.PromotionInformationRepository;
import store.service.parse.InputParser;
import store.service.parse.OutputParser;
import store.service.parse.ProductsParser;
import store.service.parse.PromotionsParser;
import store.service.validation.InputValidationService;
import store.service.validation.ProductsValidationService;
import store.service.validation.PromotionValidationService;

class StoreServiceTest {
    StoreService storeService;
    ProductInformationRepository productInformationRepository;
    InputParser inputParser;
    OutputParser outputParser;
    ProductsParser productsParser;
    PromotionsParser promotionsParser;

    @BeforeEach
    void setUp() {
        productInformationRepository = new ProductInformationRepository();
        inputParser = new InputParser(new InputValidationService());
        outputParser = new OutputParser();
        PromotionInformationRepository promotionInformationRepository = new PromotionInformationRepository();
        productsParser = new ProductsParser(productInformationRepository, promotionInformationRepository, new ProductsValidationService(promotionInformationRepository));
        promotionsParser = new PromotionsParser(promotionInformationRepository, new PromotionValidationService());
        storeService = new StoreService(productInformationRepository, inputParser, outputParser, productsParser, promotionsParser);
    }
}