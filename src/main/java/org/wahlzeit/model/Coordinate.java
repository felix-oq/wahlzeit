package org.wahlzeit.model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a point in a three dimensional space.
 */
public class Coordinate {

    private double x;
    private double y;
    private double z;

    /**
     * Creates a default coordinate instance where its x-, y- and z-components are initialized with zero.
     * @methodtype constructor
     */
    public Coordinate() {
        this.x = 0.d;
        this.y = 0.d;
        this.z = 0.d;
    }

    /**
     * Creates a coordinate instance using the provided arguments as x-, y- and z-components.
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     * @methodtype constructor
     */
    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a coordinate instance by reading the x-, y- and z-components from the provided result set.
     * @param rset the result set to read the components from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws NullPointerException if the provided argument is null
     * @methodtype constructor
     */
    public Coordinate(ResultSet rset) throws SQLException {
        this(rset.getDouble("coordinate_x"),
                rset.getDouble("coordinate_y"),
                rset.getDouble("coordinate_z"));
    }

    /**
     * Writes the contents of this coordinate on the provided result set.
     * @param rset the result set to write the values on
     * @throws SQLException if the necessary values cannot be written on the provided result set
     * @throws NullPointerException if the argument is null
     */
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateDouble("coordinate_x", getX());
        rset.updateDouble("coordinate_y", getY());
        rset.updateDouble("coordinate_z", getZ());
    }

    /**
     * Computes the euclidean distance between this coordinate and the entered one.
     * @param otherCoordinate the other coordinate to which he distance is computed
     * @return the distance between the two coordinates
     * @throws NullPointerException if the given argument is null
     */
    public double getDistance(Coordinate otherCoordinate) {
        double differenceInX = this.getX() - otherCoordinate.getX();
        double differenceInY = this.getY() - otherCoordinate.getY();
        double differenceInZ = this.getZ() - otherCoordinate.getZ();

        return Math.sqrt(differenceInX * differenceInX
                + differenceInY * differenceInY
                + differenceInZ * differenceInZ);
    }

    /**
     * Checks if this coordinate is equal to the given one. They are considered equal iff their x-, y- and z-components
     * are equal.
     * @param otherCoordinate the other coordinate which is checked to be equal
     * @return true if they are equal, false otherwise
     */
    public boolean isEqual(Coordinate otherCoordinate) {
        if (otherCoordinate == null) return false;

        return getX() == otherCoordinate.getX()
                && this.getY() == otherCoordinate.getY()
                && this.getZ() == otherCoordinate.getZ();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Coordinate) {
            return this.isEqual((Coordinate) object);
        } else {
            return false;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
