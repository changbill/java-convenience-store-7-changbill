package store.service.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.model.administrator.GeneralStock;
import store.model.administrator.ProductInformation;
import store.model.administrator.ProductNameAndPrice;
import store.model.administrator.PromotionStock;
import store.model.administrator.Stock;
import store.util.ProductDto;

public class OutputParser {

    public List<String> parseProductsToString(List<ProductDto> productDtos) {
        List<String> result = new ArrayList<>();
        for (ProductDto productDto : productDtos) {
            ProductNameAndPrice productNameAndPrice = productDto.productNameAndPrice();
            String name = productNameAndPrice.getName();
            long price = productNameAndPrice.getPrice();

            ProductInformation productInformation = productDto.productInformation();

            promotionStockMessage(productInformation, name, price).ifPresent(result::add);
            generalStockMessage(productInformation, name, price).ifPresent(result::add);
        }
        return result;
    }

    private static Optional<String> generalStockMessage(ProductInformation productInformation, String name, long price) {
        GeneralStock generalStock = productInformation.getGeneralStock();
        if (generalStock != null) {
            return Optional.of(parseToStockMessage(generalStock, name, price));
        }
        return Optional.empty();
    }

    private static Optional<String> promotionStockMessage(ProductInformation productInformation, String name, long price) {
        PromotionStock promotionStock = productInformation.getPromotionStock();
        if (promotionStock != null) {
            return Optional.of(parseToStockMessage(promotionStock, name, price)
                    + promotionStock.getPromotion().getPromotionName());
        }
        return Optional.empty();
    }

    private static String parseToStockMessage(Stock stock, String name, long price) {
        if (stock.getQuantity() == 0) {
            return "- " + name + " " + String.format("%,d", price) + "원" + " " + "재고 없음" + " ";
        }

        return "- " + name + " " + String.format("%,d", price) + "원" + " " + stock.getQuantity() + "개" + " ";
    }
}
