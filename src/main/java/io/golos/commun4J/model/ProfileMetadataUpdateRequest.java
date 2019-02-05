package io.golos.commun4J.model;

import com.memtrip.eos.abi.writer.Abi;
import com.memtrip.eos.abi.writer.ChildCompress;
import com.memtrip.eos.abi.writer.NameCompress;

import org.jetbrains.annotations.NotNull;

@Abi
public class ProfileMetadataUpdateRequest {
    @NotNull private String name;
    @NotNull private ProfileMetadata metadata;

    public ProfileMetadataUpdateRequest(@NotNull String name, @NotNull ProfileMetadata metadata) {
        this.name = name;
        this.metadata = metadata;
    }

    @NotNull
    @NameCompress
    public String getName() {
        return name;
    }

    @NotNull
    @ChildCompress
    public ProfileMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "ProfileMetadataUpdateRequest{" +
                "name='" + name + '\'' +
                ", metadata=" + metadata +
                '}';
    }
}
