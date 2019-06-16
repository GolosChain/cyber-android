package io.golos.cyber4j.model;

import io.golos.sharedmodel.CyberName;

public class PinResult {
    private CyberName pinner;
    private CyberName pinning;

    public PinResult(CyberName pinner, CyberName pinning) {
        this.pinner = pinner;
        this.pinning = pinning;
    }

    public CyberName getPinner() {
        return pinner;
    }

    public void setPinner(CyberName pinner) {
        this.pinner = pinner;
    }

    public CyberName getPinning() {
        return pinning;
    }

    public void setPinning(CyberName pinning) {
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
