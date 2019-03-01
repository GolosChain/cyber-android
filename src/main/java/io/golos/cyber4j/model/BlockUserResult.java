package io.golos.cyber4j.model;

public class BlockUserResult {
    private CyberName blocker;
    private CyberName blocking;

    public BlockUserResult(CyberName blocker, CyberName blocking) {
        this.blocker = blocker;
        this.blocking = blocking;
    }

    @Override
    public String toString() {
        return "BlockUserResult{" +
                "blocker=" + blocker +
                ", blocking=" + blocking +
                '}';
    }

    public CyberName getBlocker() {
        return blocker;
    }

    public void setBlocker(CyberName blocker) {
        this.blocker = blocker;
    }

    public CyberName getBlocking() {
        return blocking;
    }

    public void setBlocking(CyberName blocking) {
        this.blocking = blocking;
    }
}
