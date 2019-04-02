package io.golos.cyber4j.model;

import java.util.List;

public class DiscussionCreateMetadata {
    private List<EmbedmentsUrl> embeds;
    private List<String> tags;


    public DiscussionCreateMetadata(List<EmbedmentsUrl> embeds, List<String> tags) {
        this.embeds = embeds;
        this.tags = tags;
    }

    public List<EmbedmentsUrl> getEmbeds() {
        return embeds;
    }

    public void setEmbeds(List<EmbedmentsUrl> embeds) {
        this.embeds = embeds;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "DiscussionCreateMetadata{" +
                "embeds=" + embeds +
                ", tags=" + tags +
                '}';
    }

    public static class EmbedmentsUrl {
        private String url;

        public EmbedmentsUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        @Override
        public String toString() {
            return "EmbedmentsUrl{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }
}
