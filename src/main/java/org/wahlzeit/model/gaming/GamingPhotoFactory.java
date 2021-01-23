package org.wahlzeit.model.gaming;

import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoId;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * This class takes care of instantiating {@link GamingPhoto}.
 */
public class GamingPhotoFactory extends PhotoFactory {

    @Override
    public GamingPhoto createPhoto() {
        return new GamingPhoto(createDummyVideoGame());
    }

    @Override
    public GamingPhoto createPhoto(PhotoId id) {
        return new GamingPhoto(id, createDummyVideoGame());
    }

    @Override
    public GamingPhoto createPhoto(ResultSet rs) throws SQLException {
        return new GamingPhoto(rs);
    }

    /**
     * Temporary until the ui has been updated. The video game related data should be gotten from there.
     */
    private VideoGame createDummyVideoGame() {
        Random random = new Random();
        return VideoGameManager.getInstance().createInstance("Dummy-" + random.nextInt(),
                "",
                new Date(random.nextInt()));
    }

}
