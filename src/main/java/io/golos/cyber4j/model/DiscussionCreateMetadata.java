package io.golos.cyber4j.model;

import java.util.List;

public class DiscussionCreateMetadata {
    private List<EmbedmentsUrl> embeds;

    public DiscussionCreateMetadata(List<EmbedmentsUrl> embeds) {
        this.embeds = embeds;
    }

    public List<EmbedmentsUrl> getEmbeds() {
        return embeds;
    }

    @Override
    public String toString() {
        return "DiscussionCreateMetadata{" +
                "embeds=" + embeds +
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
