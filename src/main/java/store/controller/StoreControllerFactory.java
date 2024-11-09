package store.controller;

import store.repository.StoreRepository;
import store.service.parse.InputParser;
import store.service.parse.OutputParser;
import store.service.parse.ProductsParser;
import store.service.StoreService;
import store.service.parse.PromotionsParser;
import store.service.validation.InputValidationService;
import store.service.validation.ProductsValidationService;
import store.service.validation.PromotionValidationService;

public class StoreControllerFactory {

    public static StoreController getStoreController() {
        StoreRepository storeRepository = new StoreRepository();
        ProductsValidationService productsValidationService = new ProductsValidationService();
        PromotionValidationService promotionValidationService = new PromotionValidationService();
        InputValidationService inputValidationService = new InputValidationService();
        InputParser inputParser = new InputParser(productInformationRepository, inputValidationService);
        OutputParser outputParser = new OutputParser();
        ProductsParser productsParser = new ProductsParser(storeRepository, productsValidationService);
        PromotionsParser promotionsParser = new PromotionsParser(storeRepository, promotionValidationService);
        StoreService storeService = new StoreService(storeRepository, outputParser, productsParser, promotionsParser);
        return new StoreController(storeService);
    }
}
