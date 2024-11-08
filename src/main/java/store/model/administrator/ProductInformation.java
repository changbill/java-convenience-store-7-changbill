package store.model.administrator;

import static store.exception.ProductsFileExceptionMessage.*;

import store.exception.WrongProductsFileException;

public class ProductInformation {
    private GeneralStock generalStock;
    private PromotionStock promotionStock;

    public ProductInformation(GeneralStock generalStock) {
        this.generalStock = generalStock;
    }

    public ProductInformation(PromotionStock promotionStock) {
        this.generalStock = GeneralStock.of(0);
        this.promotionStock = promotionStock;
    }

    public ProductInformation addProductInformation(GeneralStock generalStock) {
        if(this.generalStock.getQuantity() != 0) {
            throw new WrongProductsFileException(PRODUCTS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION);
        }
        this.generalStock = generalStock;
        return this;
    }

    public ProductInformation addProductInformation(PromotionStock promotionStock) {
        this.promotionStock = promotionStock;
        return this;
    }

    public GeneralStock getGeneralStock() {
        return generalStock;
    }

    public PromotionStock getPromotionStock() {
        return promotionStock;
    }
}
