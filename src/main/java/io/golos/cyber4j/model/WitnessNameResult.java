package io.golos.cyber4j.model;

import java.util.Objects;

public class WitnessNameResult {
    private CyberName witness;

    public WitnessNameResult(CyberName witness) {
        this.witness = witness;
    }

    public CyberName getWitness() {
        return witness;
    }

    public void setWitness(CyberName witness) {
        this.witness = witness;
    }

    @Override
    public String toString() {
        return "WitnessNameResult{" +
                "witness=" + witness +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WitnessNameResult that = (WitnessNameResult) o;
        return Objects.equals(witness, that.witness);
    }

    @Override
    public int hashCode() {
        return Objects.hash(witness);
    }
}
