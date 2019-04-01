package io.golos.cyber4j.model;

public class OEmbedResult {
    private String type;
    private String version;
    private String title;
    private String url;
    private String provider_name;
    private String description;
    private String thumbnail_url;

    public OEmbedResult(String type, String version, String title, String url, String provider_name, String description, String thumbnail_url) {
        this.type = type;
        this.version = version;
        this.title = title;
        this.url = url;
        this.provider_name = provider_name;
        this.description = description;
        this.thumbnail_url = thumbnail_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    @Override
    public String toString() {
        return "OEmbedResult{" +
                "type='" + type + '\'' +
                ", version='" + version + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", provider_name='" + provider_name + '\'' +
                ", description='" + description + '\'' +
                ", thumbnail_url='" + thumbnail_url + '\'' +
                '}';
    }
}
