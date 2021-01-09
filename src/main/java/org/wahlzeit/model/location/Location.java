package org.wahlzeit.model.location;

import org.wahlzeit.utils.Assertions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * This class represents a location in the real world.
 */
public class Location {

    public Coordinate coordinate;

    /**
     * Creates a default location instance where its coordinate is chosen to be cartesian and its components are
     * initialized with zeros.
     * @methodtype constructor
     */
    public Location() {
        this.coordinate = CartesianCoordinate.getValueObject();
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
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types
     * @throws IndexOutOfBoundsException if the ordinal number of the coordinate type in the result set is out of bounds
     * @methodtype constructor
     */
    public Location(ResultSet rset) throws SQLException {
        Assertions.checkNotNull(rset, "The entered result set must not be null");
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "coordinate_type", Types.INTEGER);

        int coordinateTypeOrdinalNumber = rset.getInt("coordinate_type");
        CoordinateType[] coordinateTypes = CoordinateType.values();

        Assertions.checkInRange(coordinateTypeOrdinalNumber, coordinateTypes.length,
                "The ordinal number for the coordinate type in the given result set is not in range");

        CoordinateType coordinateType = CoordinateType.values()[coordinateTypeOrdinalNumber];

        this.coordinate = readCoordinate(rset, coordinateType);
    }

    private Coordinate readCoordinate(ResultSet rset, CoordinateType coordinateType) throws SQLException {
        // could be handled by an own factory class in the future
        switch(coordinateType) {
            case Cartesian: {
                return CartesianCoordinate.getValueObject(rset);
            }
            case Spheric: {
                return SphericCoordinate.getValueObject(rset);
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
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types
     * @throws NullPointerException if the argument is null
     */
    public void writeOn(ResultSet rset) throws SQLException {
        coordinate.writeOn(rset);
    }
}
