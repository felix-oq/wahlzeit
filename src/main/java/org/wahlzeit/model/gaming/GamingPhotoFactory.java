package org.wahlzeit.model.gaming;

import org.wahlzeit.model.Photo;
import org.wahlzeit.model.PhotoFactory;
import org.wahlzeit.model.PhotoId;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class takes care of instantiating {@link GamingPhoto}.
 */
public class GamingPhotoFactory extends PhotoFactory {

    @Override
    public Photo createPhoto() {
        return new GamingPhoto();
    }

    @Override
    public Photo createPhoto(PhotoId id) {
        return new GamingPhoto(id);
    }

    @Override
    public Photo createPhoto(ResultSet rs) throws SQLException {
        return new GamingPhoto(rs);
    }

}
