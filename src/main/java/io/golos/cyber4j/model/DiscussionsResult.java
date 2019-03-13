package io.golos.cyber4j.model;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DiscussionsResult {
    @NonNls
    private List<CyberDiscussion> items;
    @NonNls
    private String sequenceKey;

    public DiscussionsResult(@NonNls List<CyberDiscussion> items, @NonNls String sequenceKey) {
        this.items = items;
        this.sequenceKey = sequenceKey;
    }

    @NonNls
    public List<CyberDiscussion> getItems() {
        return items;
    }

    public void setItems(List<CyberDiscussion> items) {
        this.items = items;
    }

    @NonNls
    public String getSequenceKey() {
        return sequenceKey;
    }

    public void setSequenceKey(@Nullable String sequenceKey) {
        this.sequenceKey = sequenceKey;
    }

    @Override
    public String toString() {
        return "DiscussionsResult{" +
                "items=" + items +
                ", sequenceKey='" + sequenceKey + '\'' +
                '}';
    }
}
