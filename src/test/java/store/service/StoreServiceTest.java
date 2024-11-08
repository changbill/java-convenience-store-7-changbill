package store.service;

import org.junit.jupiter.api.BeforeEach;
import store.repository.StoreRepository;
import store.service.parse.OutputParser;
import store.service.parse.ProductsParser;
import store.service.parse.PromotionsParser;
import store.service.validation.ProductsValidationService;
import store.service.validation.PromotionValidationService;

class StoreServiceTest {
    StoreService storeService;
    StoreRepository storeRepository;
    OutputParser outputParser;
    ProductsParser productsParser;
    PromotionsParser promotionsParser;

    @BeforeEach
    void setUp() {
        storeRepository = new StoreRepository();
        outputParser = new OutputParser();
        productsParser = new ProductsParser(storeRepository, new ProductsValidationService());
        promotionsParser = new PromotionsParser(storeRepository, new PromotionValidationService());
        storeService = new StoreService(storeRepository, outputParser, productsParser, promotionsParser);
    }
}