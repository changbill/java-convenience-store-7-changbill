package store.model.administrator;

public class ProductInformation {
    private GeneralStock generalStock;
    private PromotionStock promotionStock;

    public ProductInformation(GeneralStock generalStock) {
        this.generalStock = generalStock;
    }

    public ProductInformation(PromotionStock promotionStock) {
        this.promotionStock = promotionStock;
    }

    public ProductInformation addProductInformation(GeneralStock generalStock) {
        this.generalStock = generalStock;
        return this;
    }

    public ProductInformation addProductInformation(PromotionStock promotionStock) {
        this.promotionStock = promotionStock;
        return this;
    }
}
