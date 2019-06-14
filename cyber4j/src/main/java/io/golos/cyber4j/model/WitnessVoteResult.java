package io.golos.cyber4j.model;

public class WitnessVoteResult {
    private CyberName voter;
    private CyberName witness;

    public WitnessVoteResult(CyberName voter, CyberName witness) {
        this.voter = voter;
        this.witness = witness;
    }

    public CyberName getVoter() {
        return voter;
    }

    public void setVoter(CyberName voter) {
        this.voter = voter;
    }

    public CyberName getWitness() {
        return witness;
    }

    public void setWitness(CyberName witness) {
        this.witness = witness;
    }

    @Override
    public String toString() {
        return "WitnessVoteResult{" +
                "voter=" + voter +
                ", witness=" + witness +
                '}';
    }
}
