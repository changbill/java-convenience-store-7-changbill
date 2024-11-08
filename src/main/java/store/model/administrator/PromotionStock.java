package store.model.administrator;

public class PromotionStock extends Stock {
    private final PromotionInformation promotion;

    private PromotionStock(long quantity, PromotionInformation promotion) {
        super(quantity);
        this.promotion = promotion;
    }

    public static PromotionStock of(long quantity, PromotionInformation promotion) {
        return new PromotionStock(quantity, promotion);
    }

    public PromotionInformation getPromotion() {
        return promotion;
    }
}
