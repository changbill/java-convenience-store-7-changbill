package store.model.dto.orderCalculationResponse;

import store.model.dto.ProductOrderDto;
import store.util.ResponseMessage;

public class TakeExtraBenefitResponse extends OrderCalculationResponse {
    private final long benefitQuantity;

    private TakeExtraBenefitResponse(ProductOrderDto productOrderDto, long benefitQuantity) {
        super(ResponseMessage.TAKE_EXTRA_BENEFIT, productOrderDto);
        this.benefitQuantity = benefitQuantity;
    }

    public static TakeExtraBenefitResponse of(ProductOrderDto productOrderDto, long benefitQuantity) {
        return new TakeExtraBenefitResponse(productOrderDto, benefitQuantity);
    }

    public long getBenefitQuantity() {
        return benefitQuantity;
    }
}
