package org.wahlzeit.model.location;

import org.wahlzeit.utils.Assertions;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents a point in a three dimensional space using spheric coordinates.
 */
public class SphericCoordinate extends AbstractCoordinate {

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

        // invariant
        assertClassInvariants();
    }

    /**
     * Creates a spheric coordinate instance using the provided arguments as phi-, theta- and radius-components.
     * @param phi the phi-component in radians
     * @param theta the theta-component in radians
     * @param radius the radius-component in radians
     * @throws IllegalArgumentException if any of the arguments is not finite or if the radius is negative
     * @methodtype constructor
     */
    public SphericCoordinate(double phi, double theta, double radius) {
        // pre-condition
        Assertions.checkDoubleArgumentIsFinite(phi, "The entered phi-component must be finite");
        Assertions.checkDoubleArgumentIsFinite(theta, "The entered theta-component must be finite");
        Assertions.checkDoubleArgumentIsFinite(radius, "The entered radius-component must be finite");
        if (radius < 0) throw new IllegalArgumentException("The entered radius-component must be non-negative");

        this.phi = phi;
        this.theta = theta;
        this.radius = radius;

        // invariant
        assertClassInvariants();
    }

    /**
     * Creates a spheric coordinate instance by reading the first, second and third coordinate-components from the
     * provided result set and interpreting them as phi, theta and radius.
     * @param rset the result set to read the components from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types or if the value for the radius is negative
     * @throws NullPointerException if the provided argument is null
     * @methodtype constructor
     */
    public SphericCoordinate(ResultSet rset) throws SQLException {
        // pre-condition
        Assertions.checkNotNull(rset, "The entered result set must not be null");
        assertResultSetHasCoordinateColumns(rset);

        this.phi = rset.getDouble("coordinate_1");
        this.theta = rset.getDouble("coordinate_2");
        this.radius = rset.getDouble("coordinate_3");

        // post-condition
        if (this.radius < 0)
            throw new IllegalArgumentException("The value for the radius-component must not be negative");

        // invariant
        assertClassInvariants();
    }

    @Override
    protected void assertClassInvariants() {
        assert Double.isFinite(phi) : "The phi-component has to be finite";
        assert Double.isFinite(theta) : "The theta-component has to be finite";
        assert Double.isFinite(radius) : "The radius-component has to be finite";
        assert 0 <= radius : "The radius-component has to be non-negative";
    }

    @Override
    protected void doWriteOn(ResultSet rset) throws SQLException {
        // pre-condition
        Assertions.checkNotNull(rset, "The entered result set must not be null");
        assertResultSetHasCoordinateColumns(rset);

        rset.updateDouble("coordinate_1", getPhi());
        rset.updateDouble("coordinate_2", getTheta());
        rset.updateDouble("coordinate_3", getRadius());
    }

    @Override
    protected CoordinateType doGetType() {
        return CoordinateType.Spheric;
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        // invariant
        assertClassInvariants();

        double x = radius * Math.sin(theta) * Math.cos(phi);
        double y = radius * Math.sin(theta) * Math.sin(phi);
        double z = radius * Math.cos(theta);

        CartesianCoordinate resultingCartesianCoordinate = new CartesianCoordinate(x, y, z);

        // invariant
        assertClassInvariants();

        return resultingCartesianCoordinate;
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        // invariant
        assertClassInvariants();

        return this;
    }

    public double getPhi() {
        // invariant
        assertClassInvariants();

        return phi;
    }

    public double getTheta() {
        // invariant
        assertClassInvariants();

        return theta;
    }

    public double getRadius() {
        // invariant
        assertClassInvariants();

        return radius;
    }

}
