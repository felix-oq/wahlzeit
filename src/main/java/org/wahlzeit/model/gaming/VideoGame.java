package org.wahlzeit.model.gaming;

import org.wahlzeit.utils.Assertions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Types;
import java.util.Objects;

/**
 * This class represents a specific video game and provides various data about it.
 */
public class VideoGame {

    /*
     * Documentation of the object creation process of this class.
     *
     *
     * Sequence of method calls that leads to a new instance:
     * 1. VideoGameManager::getInstance         (retrieve the singleton instance)
     * 2. VideoGameManager::createInstance      (initiate the VideoGame creation)
     * 3. VideoGameManager::getVideoGameType    (retrieve/create the type object for the demanded VideoGame instance)
     * 4. VideoGame::VideoGame                  (VideoGameManager creates a VideoGame object using the constructor of that class)
     *
     * Point in the object creation solution space:
     * 1. Delegation:       separate-object     (the VideoGameManager class)
     * 2. Selection:        on-the-spot         (createInstance-method always creates VideoGame instances)
     * 3. Configuration:    in-code             (no mapping necessary because selection is trivial)
     * 4. Instantiation:    in-code             (createInstance-method calls VideoGame constructor)
     * 5. Initialization:   by-fixed-signature  (parameters for the VideoGame constructor can be inferred from the arguments of the createInstance-method)
     * 6. Building:         default             (the complex VideoGameType hierarchy is passed as an argument to the VideoGame constructor)
     */

    private final String title;
    private final VideoGameType type;
    private final Date release;

    /**
     * Constructor that initializes the fields of this class with the provided arguments.
     * @param title the title of the video game
     * @param type the type of the video game
     * @param release the release date of the video game
     * @throws NullPointerException if any of the arguments is null
     * @throws IllegalArgumentException if the video game title is blank
     */
    public VideoGame(String title, VideoGameType type, Date release) {
        Assertions.checkNotNull(title, "The video game title must not be null");
        Assertions.checkStringArgumentIsNotBlank(title, "The video game title must not be blank");

        Assertions.checkNotNull(type, "The video game type must not be null");

        Assertions.checkNotNull(release, "The video game release date must not be null");

        this.title = title;
        this.type = type;
        this.release = release;
    }

    /**
     * Writes the contents of this video game on the provided result set.
     * @param rset the result set to write the values on
     * @throws SQLException if the necessary values cannot be written on the provided result set
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types
     * @throws NullPointerException if the argument is null
     */
    public void writeOn(ResultSet rset) throws SQLException {
        Assertions.checkNotNull(rset, "The result set must not be null");
        assertResultSetHasVideoGameColumns(rset);

        rset.updateString("game_title", title);

        String typePath = VideoGameManager.getInstance().getPathString(type);
        rset.updateString("game_type", typePath);

        rset.updateDate("game_release", release);
    }

    static void assertResultSetHasVideoGameColumns(ResultSet rset) throws SQLException {
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "game_title", Types.VARCHAR);
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "game_type", Types.VARCHAR);
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "game_release", Types.DATE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGame videoGame = (VideoGame) o;
        return Objects.equals(title, videoGame.title) &&
                Objects.equals(type, videoGame.type) &&
                Objects.equals(release, videoGame.release);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, type, release);
    }

    public String getTitle() {
        return title;
    }

    public VideoGameType getType() {
        return type;
    }

    public Date getRelease() {
        return release;
    }
}
