package store.model.administrator;

public class GeneralStock extends Stock {

    protected GeneralStock(long quantity) {
        super(quantity);
    }

    public static GeneralStock of(long quantity) {
        return new GeneralStock(quantity);
    }
}
