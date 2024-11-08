package store.model.administrator;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;

public class PromotionInformation {
    private final long buy;
    private final long get;
    private final LocalDateTime start_date;
    private final LocalDateTime end_date;

    private PromotionInformation(long buy, long get, LocalDateTime start_date, LocalDateTime end_date) {
        this.buy = buy;
        this.get = get;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public static PromotionInformation of(long buy, long get, LocalDateTime start_date, LocalDateTime end_date) {
        return new PromotionInformation(buy, get, start_date, end_date);
    }

    public boolean isAvailablePromotion() {
        LocalDateTime now = DateTimes.now();
        return start_date.isBefore(now) && end_date.isAfter(now);
    }

}
