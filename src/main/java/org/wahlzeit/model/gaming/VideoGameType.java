package org.wahlzeit.model.gaming;

import org.wahlzeit.utils.Assertions;

import java.util.*;

/**
 * This class represents a hierarchy of type objects for video games.
 */
public class VideoGameType {

    private final String name;

    private final VideoGameType superType;
    private final HashSet<VideoGameType> directSubTypes;

    /**
     * Constructs a video game type instance with a given name. The type will not have any supertype or direct subtypes.
     * @param name the name of the created video game type
     * @throws NullPointerException if the given name is null
     */
    public VideoGameType(String name) {
        this(name, null);
    }

    /**
     * Constructs a video game type instance with a given name and supertype. The type will not have any direct subtypes.
     * The created type will be registered as a subtype of the provided supertype.
     * @param name the name of the created video game type
     * @param superType the supertype of the created video game type
     * @throws NullPointerException if the given name is null
     */
    public VideoGameType(String name, VideoGameType superType) {
        Assertions.checkNotNull(name, "The entered name for the type object must not be null");

        this.name = name;
        this.superType = superType;
        this.directSubTypes = new HashSet<>();

        if (superType != null) {
            superType.addDirectSubType(this);
        }
    }

    /**
     * Checks if the given type is a subtype of this type.
     * @param subType the type to check whether it is a subtype
     * @return true if it is a subtype, false otherwise
     * @throws NullPointerException if the given subtype is null
     */
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

    /**
     * Tries to find a direct subtype with a given name.
     * @param name the name of the desired direct subtype
     * @return an optional that might hold the desired direct subtype instance.
     * @throws NullPointerException if the given name is null
     */
    public Optional<VideoGameType> getDirectSubType(String name) {
        Assertions.checkNotNull(name, "The entered name for the type object must not be null");

        return directSubTypes.stream()
                .filter((VideoGameType directSubType) -> directSubType.getName().equals(name))
                .findAny();
    }

    /**
     * @throws NullPointerException if the given subtype is null
     */
    public void addDirectSubType(VideoGameType subType) {
        Assertions.checkNotNull(subType, "The entered subtype must not be null");

        directSubTypes.add(subType);
    }
}
