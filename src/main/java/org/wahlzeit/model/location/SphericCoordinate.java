package org.wahlzeit.model.location;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a point in a three dimensional space using spheric coordinates.
 */
public class SphericCoordinate implements Coordinate {

    private double phi;
    private double theta;
    private double radius;

    /**
     * Creates a default spheric coordinate instance where its phi-, theta- and radius-components are initialized with
     * zero.
     * @methodtype constructor
     */
    public SphericCoordinate() {
        this.phi = 0.d;
        this.theta = 0.d;
        this.radius = 0.d;
    }

    /**
     * Creates a spheric coordinate instance using the provided arguments as phi-, theta- and radius-components.
     * @param phi the phi-component in radians
     * @param theta the theta-component in radians
     * @param radius the radius-component in radians
     * @methodtype constructor
     */
    public SphericCoordinate(double phi, double theta, double radius) {
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;
    }

    /**
     * Creates a spheric coordinate instance by reading the first, second and third coordinate-components from the
     * provided result set and interpreting them as phi, theta and radius.
     * @param rset the result set to read the components from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws NullPointerException if the provided argument is null
     * @methodtype constructor
     */
    public SphericCoordinate(ResultSet rset) throws SQLException {
        this(rset.getDouble("coordinate_1"),
                rset.getDouble("coordinate_2"),
                rset.getDouble("coordinate_3"));
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("coordinate_type", getType().ordinal());

        rset.updateDouble("coordinate_1", getPhi());
        rset.updateDouble("coordinate_2", getTheta());
        rset.updateDouble("coordinate_3", getRadius());
    }

    @Override
    public CoordinateType getType() {
        return CoordinateType.Spheric;
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        double x = radius * Math.sin(theta) * Math.cos(phi);
        double y = radius * Math.sin(theta) * Math.sin(phi);
        double z = radius * Math.cos(theta);

        return new CartesianCoordinate(x, y, z);
    }

    @Override
    public double getCartesianDistance(Coordinate otherCoordinate) {
        CartesianCoordinate thisCartesianCoordinate = this.asCartesianCoordinate();
        return thisCartesianCoordinate.getCartesianDistance(otherCoordinate);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        return this;
    }

    @Override
    public double getCentralAngle(Coordinate otherCoordinate) {
        // TODO
        throw new NullPointerException();
    }

    @Override
    public boolean isEqual(Coordinate otherCoordinate) {
        CartesianCoordinate thisCartesianCoordinate = this.asCartesianCoordinate();
        return thisCartesianCoordinate.isEqual(otherCoordinate);
    }

    @Override
    public boolean equals(Object object) {
        CartesianCoordinate thisCartesianCoordinate = this.asCartesianCoordinate();
        return thisCartesianCoordinate.equals(object);
    }

    @Override
    public int hashCode() {
        CartesianCoordinate thisCartesianCoordinate = this.asCartesianCoordinate();
        return thisCartesianCoordinate.hashCode();
    }

    public double getPhi() {
        return phi;
    }

    public double getTheta() {
        return theta;
    }

    public double getRadius() {
        return radius;
    }

}
