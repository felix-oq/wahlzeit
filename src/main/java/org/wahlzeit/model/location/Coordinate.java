package org.wahlzeit.model.location;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This interface represents a point in a three dimensional space.
 */
public interface Coordinate {

    /**
     * Writes the contents of this coordinate on the provided result set (including its coordinate type).
     * @param rset the result set to write the values on
     * @throws SQLException if the necessary values cannot be written on the provided result set
     * @throws NullPointerException if the argument is null
     */
    void writeOn(ResultSet rset) throws SQLException;

    CoordinateType getType();

    /**
     * Creates a cartesian coordinate that denotes the same three dimensional point in space as this coordinate.
     * @return an equivalent cartesian coordinate
     */
    CartesianCoordinate asCartesianCoordinate();

    /**
     * Computes the euclidean distance between this coordinate and the entered one.
     * @param otherCoordinate the other coordinate to which the distance is computed
     * @return the distance between the two coordinates
     * @throws NullPointerException if the given argument is null
     */
    double getCartesianDistance(Coordinate otherCoordinate);

    /**
     * Creates a spheric coordinate that denotes the same three dimensional point in space as this coordinate.
     * @return an equivalent spheric coordinate
     */
    SphericCoordinate asSphericCoordinate();

    /**
     * Computes the central angle in radians between this coordinate and the entered one.
     * @param otherCoordinate the other coordinate to which the central angle is computed
     * @return the central angle between the two coordinates in radians
     * @throws NullPointerException if the given argument is null
     */
    double getCentralAngle(Coordinate otherCoordinate);

    /**
     * Checks if this coordinate is equal to the given one. Coordinate instances of any type are considered equal iff
     * their x-, y- and z-components in their cartesian representation are equal when rounded up to their third decimal
     * place.
     * @param otherCoordinate the other coordinate which is checked to be equal
     * @return true if they are considered equal, false otherwise
     */
    boolean isEqual(Coordinate otherCoordinate);
}
