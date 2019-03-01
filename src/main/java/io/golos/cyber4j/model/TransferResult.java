package io.golos.cyber4j.model;

public class TransferResult {
    private CyberName from;
    private CyberName to;
    private CurrencyQuantity quantity;

    public TransferResult(CyberName from, CyberName to, CurrencyQuantity quantity) {
        this.from = from;
        this.to = to;
        this.quantity = quantity;
    }

    public CyberName getFrom() {
        return from;
    }

    public void setFrom(CyberName from) {
        this.from = from;
    }

    public CyberName getTo() {
        return to;
    }

    public void setTo(CyberName to) {
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
