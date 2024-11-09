package store.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import store.model.dto.ProductOrderDto;
import store.model.stock.GeneralStock;
import store.model.ProductInformation;
import store.model.setup.ProductName;
import store.model.stock.PromotionStock;
import store.model.stock.Stock;
import store.model.dto.ProductDto;

public class ProductInformationRepository {
    // TODO: ProductName String 타입으로 변경
    private final LinkedHashMap<ProductName, ProductInformation> productInformationMap =
            new LinkedHashMap<>();

    public List<ProductDto> findAllProducts() {
        List<ProductDto> productDtos = new ArrayList<>();
        productInformationMap.forEach(((productName, productInformation) ->
            productDtos.add(ProductDto.of(productName, productInformation))
        ));
        return productDtos;
    }

    public ProductInformation addProductInformation(
            ProductName productName,
            long price,
            Stock stock
    ) {
        if(stock.getClass() == GeneralStock.class) {
            return storeProductInformation(productName, price, (GeneralStock) stock);
        }
        return storeProductInformation(productName, price, (PromotionStock) stock);
    }

    private ProductInformation findProductInformation(ProductName productName) {
        if (productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName);
        }
        return null;
    }

    private ProductInformation storeProductInformation(ProductName productName, long price, PromotionStock promotionStock) {
        if(productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName).addProductInformation(promotionStock);
        }
        ProductInformation productInformation =
                new ProductInformation(price, promotionStock);
        return productInformationMap.put(productName, productInformation);
    }

    private ProductInformation storeProductInformation(ProductName productName, long price, GeneralStock generalStock) {
        if(productInformationMap.containsKey(productName)) {
            return productInformationMap.get(productName).addProductInformation(generalStock);
        }
        ProductInformation productInformation =
                new ProductInformation(price, generalStock);
        return productInformationMap.put(productName, productInformation);
    }

    public void calculateProductOrders(List<ProductOrderDto> productOrderDtos) {
        for(ProductOrderDto productOrderDto : productOrderDtos) {
            ProductInformation productInformation = productInformationMap.get(productOrderDto.productName());
            long price = productInformation.getPrice();
            PromotionStock promotionStock = productInformation.getPromotionStock();
            long orderQuantity = productOrderDto.quantity();
            if(promotionStock != null) {
                long buy = promotionStock.getPromotion().getBuy();
                long get = promotionStock.getPromotion().getGet();
                long quotient = buy + get;
                if(orderQuantity < promotionStock.getQuantity()) {
                    if(orderQuantity / quotient > 0) {
                        // 예: 2/3 X, 3/3 이상 4/3, 7/3 등등

                    }
                    // TODO: 현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N) 반환
                }

                if(orderQuantity > promotionStock.getQuantity()) {
                    // TODO: 프로모션 재고가 부족한 경우 일반 재고로 넘긴다
                    // TODO: buy와 get의 몫(quotient)
                    //  예: 2+1 -> 프로모션 재고 10개일 시 9개까지 일반 프로모션,
                    //  10개째에 일반 재고 2개와 함께 사용된다. 13개 째부터는
                    //  현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)를 반환

                }
                orderQuantity = promotionStock.decreaseQuantity(orderQuantity);
                // TODO: 프로모션 재고가 부족하여 일부 수량 프로모션 혜택없이 결제해야 하는 경우
            }
        }
    }
}
