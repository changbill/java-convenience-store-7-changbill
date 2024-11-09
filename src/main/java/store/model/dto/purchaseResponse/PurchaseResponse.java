package store.model.dto.purchaseResponse;

import store.model.dto.ProductOrderDto;
import store.util.ResponseMessage;

public abstract class PurchaseResponse {
    protected ResponseMessage responseMessage;
    protected ProductOrderDto productOrderDto;

    public PurchaseResponse(ResponseMessage responseMessage, ProductOrderDto productOrderDto) {
        this.responseMessage = responseMessage;
        this.productOrderDto = productOrderDto;
    }

    public ResponseMessage getResponseMessage() {
        return responseMessage;
    }

    public ProductOrderDto getProductOrderDto() {
        return productOrderDto;
    }
}
