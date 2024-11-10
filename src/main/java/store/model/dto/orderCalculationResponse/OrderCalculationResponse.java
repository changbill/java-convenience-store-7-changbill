package store.model.dto.orderCalculationResponse;

import store.model.dto.ProductOrderDto;
import store.util.ResponseMessage;

public abstract class OrderCalculationResponse {
    protected ResponseMessage responseMessage;
    protected ProductOrderDto productOrderDto;

    public OrderCalculationResponse(ResponseMessage responseMessage, ProductOrderDto productOrderDto) {
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
