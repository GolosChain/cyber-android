package io.golos.cyber4j.model;

public class VestingReponse {
    private CyberName owner;
    private Symbol symbol;
    private String ram_payer;

    public VestingReponse(CyberName owner, Symbol symbol, String ram_payer) {
        this.owner = owner;
        this.symbol = symbol;
        this.ram_payer = ram_payer;
    }

    public CyberName getOwner() {
        return owner;
    }

    public void setOwner(CyberName owner) {
        this.owner = owner;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getRam_payer() {
        return ram_payer;
    }

    public void setRam_payer(String ram_payer) {
        this.ram_payer = ram_payer;
    }

    static class Symbol {
        private int decs;
        private String sym;

        public Symbol(int decs, String sym) {
            this.decs = decs;
            this.sym = sym;
        }

        public int getDecs() {
            return decs;
        }

        public void setDecs(int decs) {
            this.decs = decs;
        }

        public String getSym() {
            return sym;
        }

        public void setSym(String sym) {
            this.sym = sym;
        }

        @Override
        public String toString() {
            return "Symbol{" +
                    "decs=" + decs +
                    ", sym='" + sym + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VestingReponse{" +
                "owner=" + owner +
                ", symbol=" + symbol +
                ", ram_payer='" + ram_payer + '\'' +
                '}';
    }
}
