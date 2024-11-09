package store.controller;

import java.util.List;
import store.model.dto.purchaseResponse.PurchaseResponse;
import store.service.StoreService;

public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    public void savePromotionInformation(List<String> rawPromotionInformations) {
        storeService.savePromotionInformation(rawPromotionInformations);
    }

    public void saveProductInformation(List<String> rawProductInformations) {
        storeService.saveProductInformation(rawProductInformations);
    }

    public List<String> getProductsToString() {
        return storeService.getProductsToString();
    }

    public List<PurchaseResponse> inputPurchasingProducts(String rawPurchaseOrder) {
        return storeService.getPurchaseResult(rawPurchaseOrder);
    }
}
