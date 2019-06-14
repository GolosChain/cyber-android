package io.golos.cyber4j.model;

public class CurrencyQuantity {
    private double amount;
    private double decs;

    public CurrencyQuantity(double amount, double decs) {
        this.amount = amount;
        this.decs = decs;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDecs() {
        return decs;
    }

    public void setDecs(double decs) {
        this.decs = decs;
    }

    public double getAmountCalculated() {
        return amount / (Math.pow(10, decs));
    }

    @Override
    public String toString() {
        return "CurrencyQuantity{" +
                "amount=" + amount +
                ", decs=" + decs +
                '}';
    }
}
