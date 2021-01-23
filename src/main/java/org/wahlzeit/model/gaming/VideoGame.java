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

        Assertions.checkNotNull(type, "The video game genre must not be null");

        Assertions.checkNotNull(release, "The video game release date must not be null");

        this.title = title;
        this.type = type;
        this.release = release;
    }

    /**
     * Constructor that reads the necessary data from the provided result set.
     * @param rset the result set to read the data from
     * @throws SQLException if the result set does not provide the required data
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types
     *                                  or if the video game title contained in the result set is blank
     * @throws NullPointerException if the argument is null
     */
    public VideoGame(ResultSet rset) throws SQLException {
        Assertions.checkNotNull(rset, "The result set must not be null");
        assertResultSetHasVideoGameColumns(rset);

        this.title = rset.getString("game_title");

        Assertions.checkStringArgumentIsNotBlank(this.title, "The video game title must not be blank");

        String typePath = rset.getString("game_type");
        this.type = VideoGameManager.getInstance().getVideoGameType(typePath);

        this.release = rset.getDate("game_release");
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

    private void assertResultSetHasVideoGameColumns(ResultSet rset) throws SQLException {
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "game_title", Types.VARCHAR);
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "game_genre", Types.INTEGER);
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "game_release", Types.DATE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoGame videoGame = (VideoGame) o;
        return Objects.equals(title, videoGame.title) &&
                type == videoGame.type &&
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
