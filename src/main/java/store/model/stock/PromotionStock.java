package store.model.stock;

import store.model.setup.PromotionInformation;

public class PromotionStock extends Stock {
    private final PromotionInformation promotion;

    private PromotionStock(long quantity, PromotionInformation promotion) {
        super(quantity);
        this.promotion = promotion;
    }

    public static PromotionStock of(long quantity, PromotionInformation promotion) {
        return new PromotionStock(quantity, promotion);
    }

    public void putZeroStock() {
        long stockQuantity = super.getQuantity();
        decreaseQuantity(stockQuantity);
    }

    public PromotionInformation getPromotion() {
        return promotion;
    }
}
