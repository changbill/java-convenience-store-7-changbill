package store.service.parse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import store.model.setup.PromotionInformation;
import store.repository.PromotionInformationRepository;
import store.service.validation.PromotionValidationService;
import store.util.ParseUtil;

public class PromotionsParser {
    private static final int PROMOTION_NAME_INDEX = 0;
    private static final int BUY_INDEX = 1;
    private static final int GET_INDEX = 2;
    private static final int START_DATE_INDEX = 3;
    private static final int END_DATE_INDEX = 4;

    private final PromotionInformationRepository promotionInformationRepository;
    private final PromotionValidationService promotionValidationService;

    public PromotionsParser(
            PromotionInformationRepository promotionInformationRepository,
            PromotionValidationService promotionValidationService
    ) {
        this.promotionInformationRepository = promotionInformationRepository;
        this.promotionValidationService = promotionValidationService;
    }

    public PromotionInformation parseToPromotionInformation(List<String> rawPromotionInformation) {
        promotionValidationService.validatePromotionInformations(rawPromotionInformation);
        String promotionName = rawPromotionInformation.get(PROMOTION_NAME_INDEX);
        long buy = ParseUtil.parseToLong(rawPromotionInformation.get(BUY_INDEX));
        long get = ParseUtil.parseToLong(rawPromotionInformation.get(GET_INDEX));
        LocalDateTime startDate = LocalDate.parse(rawPromotionInformation.get(START_DATE_INDEX)).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(rawPromotionInformation.get(END_DATE_INDEX)).atStartOfDay();

        PromotionInformation promotionInformation =
                PromotionInformation.of(promotionName, buy, get, startDate, endDate);

        return promotionInformationRepository.addPromotionInformation(promotionName, promotionInformation);
    }
}
