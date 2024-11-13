package store.service.validation;

import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_FILE_EXAMPLE;
import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_NAME_FORMAT_EXCEPTION;
import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_WRONG_BUY_GET_EXCEPTION;
import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_WRONG_DATE_FORMAT_EXCEPTION;
import static store.util.ValidationRegexConstant.HANGUL_AND_ONEPLUSONE;
import static store.util.ValidationRegexConstant.HANGUL_AND_TWOPLUSONE;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import store.exception.PromotionsFileException;
import store.util.ValidationUtil;

public class PromotionValidationService {
    private static final int PROMOTION_INFORMATION_SIZE = 5;
    private static final int PROMOTION_NAME_INDEX = 0;
    private static final int PROMOTION_BUY_INDEX = 1;
    private static final int PROMOTION_GET_INDEX = 2;
    private static final int PROMOTION_START_DATE_INDEX = 3;
    private static final int PROMOTION_END_DATE_INDEX = 4;

    public void validatePromotionInformations(List<String> rawPromotionInformation) {
        if (rawPromotionInformation.size() != PROMOTION_INFORMATION_SIZE) {
            throw new PromotionsFileException(PROMOTIONS_FILE_EXAMPLE);
        }

        validatePromotionInformationName(rawPromotionInformation.get(PROMOTION_NAME_INDEX));
        validatePromotionInformationBuyAndGet(
                rawPromotionInformation.get(PROMOTION_NAME_INDEX),
                rawPromotionInformation.get(PROMOTION_BUY_INDEX),
                rawPromotionInformation.get(PROMOTION_GET_INDEX)
        );
        validatePromotionInformationDate(rawPromotionInformation.get(PROMOTION_START_DATE_INDEX));
        validatePromotionInformationDate(rawPromotionInformation.get(PROMOTION_END_DATE_INDEX));
    }

    private void validatePromotionInformationName(String rawName) {
        ValidationUtil.validateNull(rawName, new PromotionsFileException(PROMOTIONS_FILE_EXAMPLE));
        if (!rawName.matches(HANGUL_AND_TWOPLUSONE.getRegex()) &&
                !rawName.matches(HANGUL_AND_ONEPLUSONE.getRegex()) &&
                        !rawName.equals("MD추천상품") &&
                        !rawName.equals("반짝할인")) {
            throw new PromotionsFileException(PROMOTIONS_NAME_FORMAT_EXCEPTION);
        }
    }

    private void validatePromotionInformationBuyAndGet(String rawName, String rawBuy, String rawGet) {
        ValidationUtil.validateNull(rawBuy, new PromotionsFileException(PROMOTIONS_FILE_EXAMPLE));
        ValidationUtil.validateNull(rawGet, new PromotionsFileException(PROMOTIONS_FILE_EXAMPLE));

        if(rawName.equals("MD추천상품") && rawBuy.equals("1") && rawGet.equals("1")) {
            return;
        }

        if(rawName.equals("반짝할인") && rawBuy.equals("1") && rawGet.equals("1")) {
            return;
        }

        if(rawName.matches(HANGUL_AND_TWOPLUSONE.getRegex()) && rawBuy.equals("2") && rawGet.equals("1")) {
            return;
        }

        if(rawName.matches(HANGUL_AND_ONEPLUSONE.getRegex()) && rawBuy.equals("1") && rawGet.equals("1")) {
            return;
        }

        throw new PromotionsFileException(PROMOTIONS_WRONG_BUY_GET_EXCEPTION);
    }

    private void validatePromotionInformationDate(String rawDate) {
        ValidationUtil.validateNull(rawDate, new PromotionsFileException(PROMOTIONS_FILE_EXAMPLE));

        try {
            LocalDate.parse(rawDate);
        } catch(DateTimeParseException e) {
            throw new PromotionsFileException(PROMOTIONS_WRONG_DATE_FORMAT_EXCEPTION);
        }
    }
}
