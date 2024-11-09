package store.service.parse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import store.model.setup.PromotionInformation;
import store.repository.ProductInformationRepository;
import store.repository.PromotionInformationRepository;
import store.service.validation.PromotionValidationService;
import store.util.ParseUtil;

public class PromotionsParser {
    private final ProductInformationRepository productInformationRepository;
    private final PromotionInformationRepository promotionInformationRepository;
    private final PromotionValidationService promotionValidationService;

    public PromotionsParser(
            ProductInformationRepository productInformationRepository,
            PromotionInformationRepository promotionInformationRepository,
            PromotionValidationService promotionValidationService
    ) {
        this.productInformationRepository = productInformationRepository;
        this.promotionInformationRepository = promotionInformationRepository;
        this.promotionValidationService = promotionValidationService;
    }

    public PromotionInformation parseToPromotionInformation(List<String> rawPromotionInformation) {
        promotionValidationService.validatePromotionInformations(rawPromotionInformation);
        String promotionName = rawPromotionInformation.get(0);
        long buy = ParseUtil.parseToLong(rawPromotionInformation.get(1));
        long get = ParseUtil.parseToLong(rawPromotionInformation.get(2));
        LocalDateTime startDate = LocalDate.parse(rawPromotionInformation.get(3)).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(rawPromotionInformation.get(4)).atStartOfDay();

        PromotionInformation promotionInformation = PromotionInformation.of(promotionName, buy, get, startDate, endDate);

        return promotionInformationRepository.addPromotionInformation(promotionName, promotionInformation);
    }


}
