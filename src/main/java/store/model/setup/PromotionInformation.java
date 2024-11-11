package store.model.setup;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class PromotionInformation {
    private final String promotionName;
    private final long buy;
    private final long get;
    private final LocalDateTime start_date;
    private final LocalDateTime end_date;

    private PromotionInformation(String promotionName, long buy, long get, LocalDateTime start_date, LocalDateTime end_date) {
        this.promotionName = promotionName;
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public static PromotionInformation of(String promotionName, long buy, long get, LocalDateTime start_date, LocalDateTime end_date) {
        return new PromotionInformation(promotionName, buy, get, start_date, end_date);
    }
    
    public double calculateBuyGetRatio() {
        return (double) get / (double) (buy + get);
    }

    // TODO: 오늘 날짜에 적용되는 프로모션만 적용.. promotions.md 읽을 때 사용할 수 있고 없고 정하자.
    public boolean isAvailablePromotion() {
        LocalDateTime now = DateTimes.now();
        return start_date.isBefore(now) && end_date.isAfter(now);
    }

    public String getPromotionName() {
        return promotionName;
    }

    public long getBuy() {
        return buy;
    }

    public long getGet() {
        return get;
    }
}
