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

        // invariant
        assertClassInvariants();
    }

    /**
     * Creates a cartesian coordinate instance using the provided arguments as x-, y- and z-components.
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     * @throws IllegalArgumentException if any of the arguments is not finite
     * @methodtype constructor
     */
    public CartesianCoordinate(double x, double y, double z) {
        // pre-condition
        assertDoubleArgumentIsFinite(x, "The entered x-component must be finite");
        assertDoubleArgumentIsFinite(y, "The entered y-component must be finite");
        assertDoubleArgumentIsFinite(z, "The entered z-component must be finite");

        this.x = x;
        this.y = y;
        this.z = z;

        // invariant
        assertClassInvariants();
    }

    /**
     * Creates a cartesian coordinate instance by reading the first, second and third coordinate-components from the
     * provided result set and interpreting them as x, y and z.
     * @param rset the result set to read the components from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types
     * @throws NullPointerException if the provided argument is null
     * @methodtype constructor
     */
    public CartesianCoordinate(ResultSet rset) throws SQLException {
        // pre-condition
        assertNotNull(rset, "The entered result set must not be null");
        assertResultSetHasCoordinateColumns(rset);

        this.x = rset.getDouble("coordinate_1");
        this.y = rset.getDouble("coordinate_2");
        this.z = rset.getDouble("coordinate_3");

        // invariant
        assertClassInvariants();
    }

    @Override
    protected void assertClassInvariants() {
        assert Double.isFinite(x) : "The x-component has to be finite";
        assert Double.isFinite(y) : "The y-component has to be finite";
        assert Double.isFinite(z) : "The z-component has to be finite";
    }

    @Override
    protected void doWriteOn(ResultSet rset) throws SQLException {
        // pre-condition
        assertNotNull(rset, "The entered result set must not be null");
        assertResultSetHasCoordinateColumns(rset);

        rset.updateDouble("coordinate_1", getX());
        rset.updateDouble("coordinate_2", getY());
        rset.updateDouble("coordinate_3", getZ());
    }

    @Override
    protected CoordinateType doGetType() {
        return CoordinateType.Cartesian;
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        // invariant
        assertClassInvariants();

        return this;
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        // invariant
        assertClassInvariants();

        double radius = Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
        double phi = Math.atan(getY() / getX());
        double theta = Math.acos(getZ() / radius);

        SphericCoordinate resultingSphericCoordinate;

        // check if no division by zero occurred
        if (Double.isFinite(phi) && Double.isFinite(theta)) {
            resultingSphericCoordinate = new SphericCoordinate(phi, theta, radius);
        } else {
            resultingSphericCoordinate = new SphericCoordinate();
        }

        // post-condition
        assert resultingSphericCoordinate != null : "The resulting spheric coordinate must not be null";

        // invariant
        assertClassInvariants();

        return resultingSphericCoordinate;
    }

    public double getX() {
        // invariant
        assertClassInvariants();

        return x;
    }

    public double getY() {
        // invariant
        assertClassInvariants();

        return y;
    }

    public double getZ() {
        // invariant
        assertClassInvariants();

        return z;
    }
}
