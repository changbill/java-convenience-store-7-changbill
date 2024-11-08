package store.service.validation;

import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_FILE_EXAMPLE;
import static store.exception.PromotionsFileExceptionMessage.PROMOTIONS_NAME_FORMAT_EXCEPTION;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import store.exception.WrongPromotionsFileException;
import store.util.ValidationUtil;

public class PromotionValidationService {
    
    public void validatePromotionInformations(List<String> rawPromotionInformation) {
        if (rawPromotionInformation.size() != 5) {
            throw new WrongPromotionsFileException(PROMOTIONS_FILE_EXAMPLE);
        }

        validatePromotionInformationName(rawPromotionInformation.get(0));
        validatePromotionInformationBuyAndGet(
                rawPromotionInformation.get(0),
                rawPromotionInformation.get(1),
                rawPromotionInformation.get(2)
        );
        validatePromotionInformationDate(rawPromotionInformation.get(3));
        validatePromotionInformationDate(rawPromotionInformation.get(4));
    }

    private void validatePromotionInformationName(String rawName) {
        ValidationUtil.validateNull(rawName, new WrongPromotionsFileException(PROMOTIONS_NAME_FORMAT_EXCEPTION));
        if (!rawName.matches("\\p{IsHangul}+2\\+1") &&
                !rawName.matches("\\p{IsHangul}+1\\+1") &&
                        !rawName.equals("MD추천상품") &&
                        !rawName.equals("반짝할인")) {
            throw new WrongPromotionsFileException(PROMOTIONS_NAME_FORMAT_EXCEPTION);
        }
    }

    private void validatePromotionInformationBuyAndGet(String rawName, String rawBuy, String rawGet) {
        if(rawName.equals("MD추천상품") && rawBuy.equals("1") && rawGet.equals("1")) {
            return;
        }

        if(rawName.equals("반짝할인") && rawBuy.equals("1") && rawGet.equals("1")) {
            return;
        }

        if(rawName.matches("\\p{IsHangul}+2\\+1") && rawBuy.equals("2") && rawGet.equals("1")) {
            return;
        }

        if(rawName.matches("\\p{IsHangul}+1\\+1") && rawBuy.equals("1") && rawGet.equals("1")) {
            return;
        }

        throw new WrongPromotionsFileException(PROMOTIONS_NAME_FORMAT_EXCEPTION);
    }

    private void validatePromotionInformationDate(String rawDate) {
        ValidationUtil.validateNull(rawDate, new WrongPromotionsFileException(PROMOTIONS_FILE_EXAMPLE));

        try {
            LocalDate.parse(rawDate);
        } catch(DateTimeParseException e) {
            throw new WrongPromotionsFileException(PROMOTIONS_FILE_EXAMPLE);
        }
    }
}
