package store.model.dto.purchaseResponse;

import store.model.dto.ProductOrderDto;
import store.util.ResponseMessage;

public class SomeDontBenefitResponse extends PurchaseResponse {
    private final long notBenefitQuantity;

    private SomeDontBenefitResponse(ProductOrderDto productOrderDto, long notBenefitQuantity) {
        super(ResponseMessage.SOME_DONT_BENEFIT, productOrderDto);
        this.notBenefitQuantity = notBenefitQuantity;
    }

    public static SomeDontBenefitResponse of(ProductOrderDto productOrderDto, long notBenefitQuantity) {
        return new SomeDontBenefitResponse(productOrderDto, notBenefitQuantity);
    }

    public long getNotBenefitQuantity() {
        return notBenefitQuantity;
    }
}
