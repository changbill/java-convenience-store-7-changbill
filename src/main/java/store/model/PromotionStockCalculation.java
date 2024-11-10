package store.model;

import static store.exception.InputExceptionMessage.INPUT_MORE_THEN_STOCK_EXCEPTION;

import store.exception.WrongInputException;
import store.model.dto.ProductOrderDto;
import store.model.dto.orderCalculationResponse.OrderCalculationResponse;
import store.model.dto.orderCalculationResponse.OrderCalculationSuccessResponse;
import store.model.dto.orderCalculationResponse.SomeDontBenefitResponse;
import store.model.dto.orderCalculationResponse.TakeExtraBenefitResponse;
import store.model.stock.GeneralStock;
import store.model.stock.PromotionStock;

public class PromotionStockCalculation {
    private final long price;
    private final PromotionStock promotionStock;
    private final ProductOrderDto productOrder;
    private final GeneralStock generalStock;

    private PromotionStockCalculation(
            long price,
            PromotionStock promotionStock,
            ProductOrderDto productOrder,
            GeneralStock generalStock
    ) {
        this.price = price;
        this.promotionStock = promotionStock;
        this.productOrder = productOrder;
        this.generalStock = generalStock;
    }

    public static PromotionStockCalculation of(
            ProductInformation productInformation,
            ProductOrderDto productOrderDto
    ) {
        return new PromotionStockCalculation(
                productInformation.getPrice(),
                productInformation.getPromotionStock(),
                productOrderDto,
                productInformation.getGeneralStock()
        );
    }

    public OrderCalculationResponse responseOrderCalculation() {
        if (promotionStock != null) {
            return calculatePromotionStock();
        }

        validateRunOutOfStock(generalStock.getQuantity());

        return OrderCalculationSuccessResponse.of(productOrder);
    }

    public OrderCalculationResponse calculatePromotionStock() {
        long promotionStockQuantity = promotionStock.getQuantity();
        long buy = promotionStock.getPromotion().getBuy();
        long get = promotionStock.getPromotion().getGet();

        validateRunOutOfStock(promotionStockQuantity + generalStock.getQuantity());

        long quotient = buy + get;
        long providableBenefit = (long) Math.ceil(
                (double) promotionStockQuantity / (double) quotient); // 제공할 수 있는 혜택 개수
        long expectedBenefit = (long) Math.floor(
                (double) productOrder.quantity() / (double) quotient); // 사용자가 받아야하는 혜택 개수
        if (providableBenefit > expectedBenefit) {
            if (productOrder.quantity() % quotient == buy) {
                return TakeExtraBenefitResponse.of(productOrder, get);
            }
            return OrderCalculationSuccessResponse.of(productOrder);
        }
        if (providableBenefit == expectedBenefit) {
            return OrderCalculationSuccessResponse.of(productOrder);
        }
        return SomeDontBenefitResponse.of(productOrder, (expectedBenefit - providableBenefit) * quotient);
    }

    public ProductInformation updateProductInformation() {
        if(promotionStock != null) {
            validateRunOutOfStock(promotionStock.getQuantity() + generalStock.getQuantity());

            if(productOrder.quantity() > promotionStock.getQuantity()) {
                generalStock.decreaseQuantity(productOrder.quantity() - promotionStock.getQuantity());
                promotionStock.putZeroStock();

                return new ProductInformation(price, promotionStock).addProductInformation(generalStock);
            }

            promotionStock.decreaseQuantity(productOrder.quantity());
            return new ProductInformation(price, generalStock).addProductInformation(promotionStock);
        }

        validateRunOutOfStock(generalStock.getQuantity());
        generalStock.decreaseQuantity(productOrder.quantity());

        return new ProductInformation(price, promotionStock).addProductInformation(generalStock);
    }

    private void validateRunOutOfStock(long stockQuantity) {
        if(stockQuantity < productOrder.quantity()) {
            throw new WrongInputException(INPUT_MORE_THEN_STOCK_EXCEPTION);
        }
    }
}
