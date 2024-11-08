package store.service.parse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import store.model.administrator.PromotionInformation;
import store.repository.StoreRepository;
import store.service.validation.PromotionValidationService;
import store.util.ParseUtil;

public class PromotionsParser {
    private final StoreRepository storeRepository;
    private final PromotionValidationService promotionValidationService;

    public PromotionsParser(StoreRepository storeRepository, PromotionValidationService promotionValidationService) {
        this.storeRepository = storeRepository;
        this.promotionValidationService = promotionValidationService;
    }

    public PromotionInformation parseToPromotionInformation(List<String> rawPromotionInformation) {
        promotionValidationService.validatePromotionInformations(rawPromotionInformation);
        String promotionName = rawPromotionInformation.get(0);
        long buy = ParseUtil.parseToLong(rawPromotionInformation.get(1));
        long get = ParseUtil.parseToLong(rawPromotionInformation.get(2));
        LocalDateTime startDate = LocalDate.parse(rawPromotionInformation.get(3)).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(rawPromotionInformation.get(4)).atStartOfDay();

        PromotionInformation promotionInformation = PromotionInformation.of(buy, get, startDate, endDate);

        return storeRepository.addPromotionInformation(promotionName, promotionInformation);
    }


}
