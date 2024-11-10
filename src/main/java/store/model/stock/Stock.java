package store.model.stock;

public abstract class Stock {
    private long quantity;

    protected Stock(long quantity) {
        this.quantity = quantity;
    }

    public long decreaseQuantity(long amount) {
        long result = this.quantity - amount;
        if(result < 0) {
            throw new RuntimeException("Stock 보다 많은 양을 decrease 시켰습니다.");
        }
        return this.quantity -= amount;
    }

    public long getQuantity() {
        return quantity;
    }
}
