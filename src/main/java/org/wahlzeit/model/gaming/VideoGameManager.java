package org.wahlzeit.model.gaming;

import org.wahlzeit.utils.Assertions;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

public class VideoGameManager {

    public static final String ROOT_TYPE_NAME = "RootType";
    public static final String PATH_SEPARATOR = "/";

    private static VideoGameManager instance = null;

    private final VideoGameType rootType;

    private VideoGameManager() {
        this.rootType = new VideoGameType(ROOT_TYPE_NAME);
    }

    public static synchronized VideoGameManager getInstance() {
        if (instance == null) {
            instance = new VideoGameManager();
        }
        return instance;
    }

    public VideoGame createInstance(String title, String typePath, Date release) {
        VideoGameType type = getVideoGameType(typePath);
        return new VideoGame(title, type, release);
    }

    public VideoGame createInstance(ResultSet rset) throws SQLException {
        return new VideoGame(rset);
    }

    public VideoGameType getVideoGameType(String typePath) {
        Assertions.checkNotNull(typePath, "The entered type path must not be null");

        String[] typeNames = getTypeNames(typePath);
        return getVideoGameType(typeNames);
    }

    private String[] getTypeNames(String pathString) {
        return pathString.split(PATH_SEPARATOR);
    }

    private VideoGameType getVideoGameType(String[] typePath) {
        VideoGameType currentType = rootType;
        for (int typePathIndex = 0; typePathIndex < typePath.length; ++typePathIndex) {
            String currentSubTypeName = typePath[typePathIndex];
            Optional<VideoGameType> subType = currentType.getDirectSubType(currentSubTypeName);

            if (!subType.isPresent()) {
                String[] remainingTypePath = Arrays.copyOfRange(typePath, typePathIndex, typePath.length);
                return insertVideoGameType(currentType, remainingTypePath);
            }

            currentType = subType.get();
        }
        return currentType;
    }

    private VideoGameType insertVideoGameType(VideoGameType root, String[] typePath) {
        VideoGameType currentType = root;
        for (String currentSubTypeName : typePath) {
            VideoGameType newType = new VideoGameType(currentSubTypeName, currentType);

            currentType = newType;
        }
        return currentType;
    }

    public String getPathString(VideoGameType type) {
        Assertions.checkNotNull(type, "The entered type object must not be null");

        StringBuilder stringBuilder = new StringBuilder();

        for(VideoGameType currentType = type; currentType != rootType; currentType = currentType.getSuperType()) {
            if (currentType != type) {
                stringBuilder.insert(0, PATH_SEPARATOR);
            }
            stringBuilder.insert(0, currentType.getName());
        }

        return stringBuilder.toString();
    }
}
