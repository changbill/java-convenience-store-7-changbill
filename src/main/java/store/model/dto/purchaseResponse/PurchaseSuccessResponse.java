package store.model.dto.purchaseResponse;

import store.model.dto.ProductOrderDto;
import store.util.ResponseMessage;

public class PurchaseSuccessResponse extends PurchaseResponse {

    private PurchaseSuccessResponse(ProductOrderDto productOrderDto) {
        super(ResponseMessage.SUCCESS, productOrderDto);
    }

    public static PurchaseSuccessResponse of(ProductOrderDto productOrderDto) {
        return new PurchaseSuccessResponse(productOrderDto);
    }
}
