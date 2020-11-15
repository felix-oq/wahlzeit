package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a location in the real world.
 */
public class Location {

    public Coordinate coordinate;

    /**
     * Creates a default location instance where its coordinate are initialized with zeros.
     * @methodtype constructor
     */
    public Location() {
        this.coordinate = new Coordinate();
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
     * @methodtype constructor
     */
    public Location(ResultSet rset) throws SQLException {
        this(new Coordinate(rset));
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
