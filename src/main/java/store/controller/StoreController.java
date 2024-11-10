package store.controller;

import java.util.List;
import store.model.dto.ProductOrderDto;
import store.model.dto.ReceiptDto;
import store.model.dto.orderCalculationResponse.OrderCalculationResponse;
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

    public List<OrderCalculationResponse> getCalculationResponse(String rawPurchaseOrder) {
        return storeService.getCalculationResponse(rawPurchaseOrder);
    }

    public List<ReceiptDto> inputOrders(List<ProductOrderDto> orders) {
        return storeService.putOrders(orders);
    }
}
