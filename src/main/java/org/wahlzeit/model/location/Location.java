package org.wahlzeit.model.location;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a location in the real world.
 */
public class Location {

    public Coordinate coordinate;

    /**
     * Creates a default location instance where its coordinate is chosen to be cartesian and its are initialized with
     * zeros.
     * @methodtype constructor
     */
    public Location() {
        this.coordinate = new CartesianCoordinate();
    }

    /**
     * Creates a location instance where the provided coordinate is used for initialization.
     * @param coordinate the coordinate of the location to be created
     * @methodtype constructor
     */
    public Location(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    /**
     * Creates a location by reading the necessary data from the provided result set.
     * @param rset the result set to read the necessary data from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws NullPointerException if the provided argument is null
     * @throws IndexOutOfBoundsException if the ordinal number of the coordinate type in the result set is out of bounds
     * @methodtype constructor
     */
    public Location(ResultSet rset) throws SQLException {
        int coordinateTypeOrdinalNumber = rset.getInt("coordinate_type");
        CoordinateType coordinateType = CoordinateType.values()[coordinateTypeOrdinalNumber];

        this.coordinate = readCoordinate(rset, coordinateType);
    }

    private Coordinate readCoordinate(ResultSet rset, CoordinateType coordinateType) throws SQLException {
        // could be handled by an own factory class in the future
        switch(coordinateType) {
            case Cartesian: {
                return new CartesianCoordinate(rset);
            }
            case Spheric: {
                return new SphericCoordinate(rset);
            }
            default: {
                // should not happen if the cases above are complete
                throw new RuntimeException("Encountered an unhandled CoordinateType: " + coordinateType.name());
            }
        }
    }

    /**
     * Writes the contents of this location on the provided result set.
     * @param rset the result set to write the values on
     * @throws SQLException if the necessary values cannot be written on the provided result set
     * @throws NullPointerException if the argument is null
     */
    public void writeOn(ResultSet rset) throws SQLException {
        coordinate.writeOn(rset);
    }
}
