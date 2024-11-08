package store.model.administrator;

public abstract class Stock {
    private long quantity;

    protected Stock(long quantity) {
        this.quantity = quantity;
    }

    public long decreaseQuantity(long amount) {
        return this.quantity -= amount;
    }

    public long getQuantity() {
        return quantity;
    }
}
