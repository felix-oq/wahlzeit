package org.wahlzeit.model.gaming;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

/**
 * This class represents a specific video game and provides various data about it.
 */
public class VideoGame {

    private final String title;
    private final VideoGameGenre genre;
    private final Date release;

    /**
     * Default constructor that sets all fields to null.
     */
    public VideoGame() {
        this.title = null;
        this.genre = null;
        this.release = null;
    }

    /**
     * Constructor that initializes the fields of this class with the provided arguments.
     * @param title the title of the video game
     * @param genre the genre of the video game
     * @param release the release date of the video game
     */
    public VideoGame(String title, VideoGameGenre genre, Date release) {
        this.title = title;
        this.genre = genre;
        this.release = release;
    }

    /**
     * Constructor that reads the necessary data from the provided result set.
     * @param rset the result set to read the data from
     * @throws SQLException if the result set does not provide the required data
     * @throws NullPointerException if the argument is null
     */
    public VideoGame(ResultSet rset) throws SQLException {
        this.title = rset.getString("game_title");
        String genreString = rset.getString("game_genre");
        this.genre = (genreString == null) ? null : VideoGameGenre.valueOf(genreString);
        this.release = rset.getDate("game_release");
    }

    /**
     * Writes the contents of this video game on the provided result set.
     * @param rset the result set to write the values on
     * @throws SQLException if the necessary values cannot be written on the provided result set
     * @throws NullPointerException if the argument is null
     */
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateString(title, "game_title");
        rset.updateString(genre.name(), "game_genre");
        rset.updateDate("game_release", release);
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
