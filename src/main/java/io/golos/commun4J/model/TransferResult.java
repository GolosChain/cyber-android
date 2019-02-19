package io.golos.commun4J.model;

public class TransferResult {
    private CommunName from;
    private CommunName to;
    private CurrencyQuantity quantity;

    public TransferResult(CommunName from, CommunName to, CurrencyQuantity quantity) {
        this.from = from;
        this.to = to;
        this.quantity = quantity;
    }

    public CommunName getFrom() {
        return from;
    }

    public void setFrom(CommunName from) {
        this.from = from;
    }

    public CommunName getTo() {
        return to;
    }

    public void setTo(CommunName to) {
        this.to = to;
    }

    public CurrencyQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(CurrencyQuantity quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "TransferResult{" +
                "from=" + from +
                ", to=" + to +
                ", quantity=" + quantity +
                '}';
    }
}
