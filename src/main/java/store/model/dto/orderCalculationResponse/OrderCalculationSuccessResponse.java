package store.model.dto.orderCalculationResponse;

import store.model.dto.ProductOrderDto;
import store.util.ResponseMessage;

public class OrderCalculationSuccessResponse extends OrderCalculationResponse {

    private OrderCalculationSuccessResponse(ProductOrderDto productOrderDto) {
        super(ResponseMessage.SUCCESS, productOrderDto);
    }

    public static OrderCalculationSuccessResponse of(ProductOrderDto productOrderDto) {
        return new OrderCalculationSuccessResponse(productOrderDto);
    }
}
