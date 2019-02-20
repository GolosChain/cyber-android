package io.golos.commun4J.model;

public class BlockUserResult {
    private CommunName blocker;
    private CommunName blocking;

    public BlockUserResult(CommunName blocker, CommunName blocking) {
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

    public CommunName getBlocker() {
        return blocker;
    }

    public void setBlocker(CommunName blocker) {
        this.blocker = blocker;
    }

    public CommunName getBlocking() {
        return blocking;
    }

    public void setBlocking(CommunName blocking) {
        this.blocking = blocking;
    }
}
