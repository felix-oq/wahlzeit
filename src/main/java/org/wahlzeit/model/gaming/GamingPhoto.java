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

    /*
     * Documentation of the object creation process of this class.
     *
     *
     * Sequence of method calls that leads to a new instance:
     * 1. GamingPhotoManager::createObject  (ask the manager to create a new object)
     * 2. PhotoManager::createObject        (GamingPhotoManager calls createObject-method of superclass)
     * 3. GamingPhotoFactory::createPhoto   (PhotoManager calls createPhoto of the GamingPhotoFactory)
     * 4. GamingPhoto::GamingPhoto          (GamingPhotoFactory creates a GamingPhoto object using the constructor of that class)
     * 5. Photo::Photo                      (GamingPhoto calls constructor of superclass)
     *
     * Point in the object creation solution space:
     * 1. Delegation:       separate-object     (the GamingPhotoManager class)
     * 2. Selection:        by-colocating       (the GamingPhotoFactory is called to create the specific type of photo)
     * 3. Configuration:    in-code             (the usage of the GamingPhotoFactory class as factory is hard-coded in PhotoFactory::getInstance)
     * 4. Instantiation:    in-code             (createPhoto-method calls GamingPhoto constructor)
     * 5. Initialization:   by-fixed-signature  (parameters for the GamingPhoto constructor can be inferred from the arguments of the createPhoto-method)
     * 6. Building:         default             (no complex building required)
     */

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

        try {
            videoGame.writeOn(rset);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new SQLException("Unable to store the video game data for the photo" +
                    " due to missing columns or wrong column types in the database", illegalArgumentException);
        }
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);

        try {
            videoGame = VideoGameManager.getInstance().createInstance(rset);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new SQLException("Unable to read the video game data for the photo" +
                    " due to missing columns, wrong column types in the database or a blank video game title",
                    illegalArgumentException);
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            throw new SQLException("Unable to interpret the video game genre that is stored in the database",
                    indexOutOfBoundsException);
        }
    }

}
