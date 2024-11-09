package store.model;

import static store.exception.InputExceptionMessage.INPUT_MORE_THEN_STOCK_EXCEPTION;

import store.exception.WrongInputException;
import store.model.dto.ProductOrderDto;
import store.model.dto.purchaseResponse.PurchaseResponse;
import store.model.dto.purchaseResponse.PurchaseSuccessResponse;
import store.model.dto.purchaseResponse.SomeDontBenefitResponse;
import store.model.dto.purchaseResponse.TakeExtraBenefitResponse;

public class PromotionStockCalculation {
    private final long price;
    private final long buy;
    private final long get;
    private final ProductOrderDto productOrder;
    private final long promotionStockQuantity;
    private final long generalStockQuantity;

    private PromotionStockCalculation(
            long price,
            long buy,
            long get,
            ProductOrderDto productOrder,
            long promotionStockQuantity,
            long generalStockQuantity
    ) {
        this.price = price;
        this.buy = buy;
        this.get = get;
        this.productOrder = productOrder;
        this.promotionStockQuantity = promotionStockQuantity;
        this.generalStockQuantity = generalStockQuantity;
    }

    public static PromotionStockCalculation of(ProductInformation productInformation, ProductOrderDto productOrderDto) {
        return new PromotionStockCalculation(
                productInformation.getPrice(),
                productInformation.getPromotionStock().getPromotion().getBuy(),
                productInformation.getPromotionStock().getPromotion().getGet(),
                productOrderDto,
                productInformation.getPromotionStock().getQuantity(),
                productInformation.getGeneralStock().getQuantity()
        );
    }

    public PurchaseResponse responseCalculation() {
        if(promotionStockQuantity + generalStockQuantity - productOrder.quantity() < 0) {
            throw new WrongInputException(INPUT_MORE_THEN_STOCK_EXCEPTION);
        }

        long quotient = buy + get;
        long providableBenefit = (long) Math.ceil((double) promotionStockQuantity / (double) quotient); // 제공할 수 있는 혜택 개수
        long expectedBenefit = (long) Math.floor((double) productOrder.quantity() / (double) quotient); // 사용자가 받아야하는 혜택 개수
        if(providableBenefit > expectedBenefit) {
            if(productOrder.quantity() % quotient == buy) {
                return TakeExtraBenefitResponse.of(productOrder, get);
            }
            return PurchaseSuccessResponse.of(productOrder);
        }
        if(providableBenefit == expectedBenefit) {
            return PurchaseSuccessResponse.of(productOrder);
        }
        return SomeDontBenefitResponse.of(productOrder, expectedBenefit - providableBenefit);
    }
}
