package org.wahlzeit.model.gaming;

import org.wahlzeit.utils.Assertions;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

/**
 * This class creates video game instances and manages their video game type instances.
 */
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

    /**
     * Creates a video game instance.
     * @param title the title of the video game
     * @param typePath the type path for the type of the video game
     * @param release the release date of the video game
     * @throws NullPointerException if any of the arguments is null
     * @throws IllegalArgumentException if the video game title is blank
     */
    public VideoGame createInstance(String title, String typePath, Date release) {
        VideoGameType type = getVideoGameType(typePath);
        return new VideoGame(title, type, release);
    }

    /**
     * Creates a video game instance by reading the necessary data from the provided result set.
     * @param rset the result set to read the data from
     * @throws SQLException if the result set does not provide the required data
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types
     *                                  or if the video game title contained in the result set is blank
     * @throws NullPointerException if the argument is null
     */
    public VideoGame createInstance(ResultSet rset) throws SQLException {
        Assertions.checkNotNull(rset, "The result set must not be null");
        VideoGame.assertResultSetHasVideoGameColumns(rset);

        String title = rset.getString("game_title");
        Assertions.checkStringArgumentIsNotBlank(title, "The video game title must not be blank");

        String typePath = rset.getString("game_type");
        Date release = rset.getDate("game_release");

        return createInstance(title, typePath, release);
    }

    /**
     * Gets an existing or creates a new video game type instance. The path determines its place in the type hierarchy,
     * the last segment of the path is the actual type name. The separator for path segments can be accessed via
     * {@value #PATH_SEPARATOR}. There will always be a root type instance as a root of the hierarchy (it should not be
     * a segment of the path). Its name can be accessed via {@value #ROOT_TYPE_NAME}.
     * @param typePath the type path that describes the desired place in the type hierarchy for the type instance to get
     * @return the desired video game type instance
     * @throws NullPointerException if the given type path is null
     */
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

    /**
     * Creates a path string from a given video game type instance which describes its place in the type hierarchy.
     * Can be used to store the type information in a database for example.
     * @param type the video game type instance to get its path for
     * @return the path of the given type
     * @throws NullPointerException if the given video game type is null
     */
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
