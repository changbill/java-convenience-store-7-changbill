package store.repository;


import java.util.HashMap;
import store.model.setup.PromotionInformation;

public class PromotionInformationRepository {
    private final HashMap<String, PromotionInformation> promotionMap = new HashMap<>();

    public PromotionInformation findPromotionInformation(String rawPromotion) {
        return promotionMap.get(rawPromotion);
    }

    public PromotionInformation addPromotionInformation(String promotionName, PromotionInformation promotionInformation) {
        if(promotionMap.containsKey(promotionName)) {
            double existingPromotion = promotionMap.get(promotionName).calculateBuyGetRatio();
            double newPromotion = promotionInformation.calculateBuyGetRatio();
            if(newPromotion <= existingPromotion) {
                return promotionMap.get(promotionName);
            }
        }
        return promotionMap.put(promotionName, promotionInformation);
    }
}
