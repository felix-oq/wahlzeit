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
    private final VideoGameGenre genre;
    private final Date release;

    /**
     * Constructor that initializes the fields of this class with the provided arguments.
     * @param title the title of the video game
     * @param genre the genre of the video game
     * @param release the release date of the video game
     * @throws NullPointerException if any of the arguments is null
     * @throws IllegalArgumentException if the video game title is blank
     */
    public VideoGame(String title, VideoGameGenre genre, Date release) {
        Assertions.checkNotNull(title, "The video game title must not be null");
        Assertions.checkStringArgumentIsNotBlank(title, "The video game title must not be blank");

        Assertions.checkNotNull(genre, "The video game genre must not be null");

        Assertions.checkNotNull(release, "The video game release date must not be null");

        this.title = title;
        this.genre = genre;
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
     * @throws IndexOutOfBoundsException if the ordinal number of the video game genre in the result set is out of bounds
     */
    public VideoGame(ResultSet rset) throws SQLException {
        Assertions.checkNotNull(rset, "The result set must not be null");
        assertResultSetHasVideoGameColumns(rset);

        this.title = rset.getString("game_title");

        Assertions.checkStringArgumentIsNotBlank(this.title, "The video game title must not be blank");

        VideoGameGenre[] videoGameGenres = VideoGameGenre.values();
        int videoGameGenreIndex = rset.getInt("game_genre");

        Assertions.checkInRange(videoGameGenreIndex, videoGameGenres.length,
                "The ordinal number for the video game genre in the given result set is not in range");

        this.genre = videoGameGenres[videoGameGenreIndex];

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
        rset.updateInt("game_genre", genre.ordinal());
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
                genre == videoGame.genre &&
                Objects.equals(release, videoGame.release);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, genre, release);
    }

    public String getTitle() {
        return title;
    }

    public VideoGameGenre getGenre() {
        return genre;
    }

    public Date getRelease() {
        return release;
    }
}
