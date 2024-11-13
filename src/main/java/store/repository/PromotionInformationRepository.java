package store.repository;


import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION;

import java.util.HashMap;
import store.exception.PromotionsFileException;
import store.model.setup.PromotionInformation;

public class PromotionInformationRepository {
    private final HashMap<String, PromotionInformation> promotionMap = new HashMap<>();

    public PromotionInformation findPromotionInformation(String rawPromotion) {
        return promotionMap.get(rawPromotion);
    }

    public PromotionInformation addPromotionInformation(
            String promotionName,
            PromotionInformation promotionInformation
    ) {
        if(promotionMap.containsKey(promotionName)) {
            throw new PromotionsFileException(PROMOTIONS_FILE_SAME_NAME_DIFFERENT_PRICE_EXCEPTION);
        }
        return promotionMap.put(promotionName, promotionInformation);
    }
}
