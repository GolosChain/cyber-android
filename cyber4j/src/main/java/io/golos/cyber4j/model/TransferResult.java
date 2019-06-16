package io.golos.cyber4j.model;

import io.golos.sharedmodel.CyberName;

public class TransferResult {
    private CyberName from;
    private CyberName to;
    private String quantity;

    public TransferResult(CyberName from, CyberName to, String quantity) {
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
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
