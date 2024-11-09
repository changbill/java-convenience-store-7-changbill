package store.model;

import static store.exception.ProductsFileExceptionMessage.*;

import store.exception.WrongProductsFileException;
import store.model.stock.GeneralStock;
import store.model.stock.PromotionStock;

public class ProductInformation {
    private final long price;
    private GeneralStock generalStock;
    private PromotionStock promotionStock;

    public ProductInformation(long price, GeneralStock generalStock) {
        this.price = price;
        this.generalStock = generalStock;
    }

    public ProductInformation(long price, PromotionStock promotionStock) {
        this.price = price;
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

    public long getPrice() {
        return price;
    }

    public GeneralStock getGeneralStock() {
        return generalStock;
    }

    public PromotionStock getPromotionStock() {
        return promotionStock;
    }
}
