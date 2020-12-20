package org.wahlzeit.model.gaming;

import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoId;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class symbolizes a gaming photo (i.e. a screenshot of a video game). It holds additional information like
 * the title of the video game, its genre and more.
 */
public class GamingPhoto extends Photo {

    /**
     * The video game of which this image is taken from.
     */
    private VideoGame videoGame;

    public GamingPhoto(VideoGame videoGame) {
        super();
        this.videoGame = videoGame;
    }

    public GamingPhoto(PhotoId myId, VideoGame videoGame) {
        super(myId);
        this.videoGame = videoGame;
    }

    public GamingPhoto(ResultSet rset) throws SQLException {
        super(rset);
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);

        // TODO exception handling
        videoGame.writeOn(rset);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);

        // TODO exception handling
        videoGame = new VideoGame(rset);
    }

}
