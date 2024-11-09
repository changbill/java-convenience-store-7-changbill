package store.service.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import store.model.stock.GeneralStock;
import store.model.ProductInformation;
import store.model.setup.ProductName;
import store.model.stock.PromotionStock;
import store.model.stock.Stock;
import store.model.dto.ProductDto;

public class OutputParser {

    public List<String> parseProductsToString(List<ProductDto> productDtos) {
        List<String> result = new ArrayList<>();
        for (ProductDto productDto : productDtos) {
            ProductName productName = productDto.productName();
            String name = productName.getName();
            long price = productDto.productInformation().getPrice();

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
