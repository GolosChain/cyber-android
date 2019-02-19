package io.golos.commun4J.model;

public class PinResult {
    private CommunName pinner;
    private CommunName pinning;

    public PinResult(CommunName pinner, CommunName pinning) {
        this.pinner = pinner;
        this.pinning = pinning;
    }

    public CommunName getPinner() {
        return pinner;
    }

    public void setPinner(CommunName pinner) {
        this.pinner = pinner;
    }

    public CommunName getPinning() {
        return pinning;
    }

    public void setPinning(CommunName pinning) {
        this.pinning = pinning;
    }

    @Override
    public String toString() {
        return "PinResult{" +
                "pinner=" + pinner +
                ", pinning=" + pinning +
                '}';
    }
}
