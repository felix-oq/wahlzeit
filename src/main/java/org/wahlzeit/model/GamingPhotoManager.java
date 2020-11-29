package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class manages the instances of {@link GamingPhoto}.
 */
public class GamingPhotoManager extends PhotoManager {

    public GamingPhotoManager() {
        super();
    }

    @Override
    protected Photo createObject(ResultSet rset) throws SQLException {
        Photo result = super.createObject(rset);

        // when using gaming photo manager, the created photo should be gaming photos respectively
        assert result instanceof GamingPhoto;

        return result;
    }

}
