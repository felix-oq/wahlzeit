package org.wahlzeit.model.gaming;

import org.wahlzeit.utils.Assertions;

import java.util.*;

public class VideoGameType {

    private final String name;

    private final VideoGameType superType;
    private final HashSet<VideoGameType> directSubTypes;

    public VideoGameType(String name) {
        this(name, null);
    }

    public VideoGameType(String name, VideoGameType superType) {
        this.name = name;
        this.superType = superType;
        this.directSubTypes = new HashSet<>();

        if (superType != null) {
            superType.addDirectSubType(this);
        }
    }

    public boolean hasSubType(VideoGameType subType) {
        Assertions.checkNotNull(subType, "The entered subtype must not be null");

        if (this.equals(subType)) {
            return true;
        }

        for (VideoGameType directSubType : getDirectSubTypes()) {
            if (directSubType.hasSubType(subType)) {
                return true;
            }
        }

        return false;
    }

    public String getName() {
        return name;
    }

    public VideoGameType getSuperType() {
        return superType;
    }

    public Iterable<VideoGameType> getDirectSubTypes() {
        return Collections.unmodifiableSet(directSubTypes);
    }

    public Optional<VideoGameType> getDirectSubType(String name) {
        return directSubTypes.stream()
                .filter((VideoGameType directSubType) -> directSubType.getName().equals(name))
                .findAny();
    }

    public void addDirectSubType(VideoGameType subType) {
        Assertions.checkNotNull(subType, "The entered subtype must not be null");

        directSubTypes.add(subType);
    }
}
