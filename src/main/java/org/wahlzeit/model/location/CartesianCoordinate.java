package org.wahlzeit.model.location;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a point in a three dimensional space using cartesian coordinates.
 */
public class CartesianCoordinate extends AbstractCoordinate {

    private double x;
    private double y;
    private double z;

    /**
     * Creates a default cartesian coordinate instance where its x-, y- and z-components are initialized with zero.
     * @methodtype constructor
     */
    public CartesianCoordinate() {
        this.x = 0.d;
        this.y = 0.d;
        this.z = 0.d;
    }

    /**
     * Creates a cartesian coordinate instance using the provided arguments as x-, y- and z-components.
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     * @methodtype constructor
     */
    public CartesianCoordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a cartesian coordinate instance by reading the first, second and third coordinate-components from the
     * provided result set and interpreting them as x, y and z.
     * @param rset the result set to read the components from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws NullPointerException if the provided argument is null
     * @methodtype constructor
     */
    public CartesianCoordinate(ResultSet rset) throws SQLException {
        this(rset.getDouble("coordinate_1"),
                rset.getDouble("coordinate_2"),
                rset.getDouble("coordinate_3"));
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);

        rset.updateDouble("coordinate_1", getX());
        rset.updateDouble("coordinate_2", getY());
        rset.updateDouble("coordinate_3", getZ());
    }

    @Override
    public CoordinateType getType() {
        return CoordinateType.Cartesian;
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        double radius = Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
        double phi = Math.atan(getY() / getX());
        double theta = Math.acos(getZ() / radius);

        return new SphericCoordinate(phi, theta, radius);
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
