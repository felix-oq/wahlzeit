package org.wahlzeit.model.location;

import org.wahlzeit.utils.Assertions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This class represents a point in a three dimensional space using cartesian coordinates.
 */
public class CartesianCoordinate extends AbstractCoordinate {

    /**
     * The number of decimal places for the cartesian coordinate components x,y,z that are considered, when two
     * coordinates are checked to be equal or not.
     */
    private static final int DECIMAL_PLACES_FOR_EQUALITY = 3;

    private double x;
    private double y;
    private double z;

    private static final HashSet<CartesianCoordinate> cartesianCoordinates = new HashSet<>();

    private CartesianCoordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        // invariant
        assertClassInvariants();
    }

    /**
     * Gets a default cartesian coordinate instance where its x-, y- and z-components are initialized with zero. The
     * resulting coordinate is the corresponding value object for the given coordinate components.
     * @methodtype constructor
     */
    public static CartesianCoordinate getValueObject() {
        return lookupValueObject(0.d, 0.d, 0.d);
    }

    /**
     * Gets a cartesian coordinate instance using the provided arguments as x-, y- and z-components. The resulting
     * coordinate is the corresponding value object for the given coordinate components.
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     * @throws IllegalArgumentException if any of the arguments is not finite
     * @methodtype constructor
     */
    public static CartesianCoordinate getValueObject(double x, double y, double z) {
        // pre-condition
        Assertions.checkDoubleArgumentIsFinite(x, "The entered x-component must be finite");
        Assertions.checkDoubleArgumentIsFinite(y, "The entered y-component must be finite");
        Assertions.checkDoubleArgumentIsFinite(z, "The entered z-component must be finite");

        return lookupValueObject(x, y, z);
    }

    /**
     * Gets a cartesian coordinate instance by reading the first, second and third coordinate-components from the
     * provided result set and interpreting them as x, y and z. The resulting coordinate is the corresponding value
     * object for the given coordinate components.
     * @param rset the result set to read the components from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types
     * @throws NullPointerException if the provided argument is null
     * @methodtype constructor
     */
    public static CartesianCoordinate getValueObject(ResultSet rset) throws SQLException {
        // pre-condition
        Assertions.checkNotNull(rset, "The entered result set must not be null");
        assertResultSetHasCoordinateColumns(rset);

        double x = rset.getDouble("coordinate_1");
        double y = rset.getDouble("coordinate_2");
        double z = rset.getDouble("coordinate_3");

        return lookupValueObject(x, y, z);
    }

    private static CartesianCoordinate lookupValueObject(double x, double y, double z) {
        final double roundedX = roundToDecimalPlaces(x, DECIMAL_PLACES_FOR_EQUALITY);
        final double roundedY = roundToDecimalPlaces(y, DECIMAL_PLACES_FOR_EQUALITY);
        final double roundedZ = roundToDecimalPlaces(z, DECIMAL_PLACES_FOR_EQUALITY);

        synchronized(cartesianCoordinates) {
            Optional<CartesianCoordinate> valueObject = cartesianCoordinates.stream()
                    .filter(cartesianCoordinate ->
                            cartesianCoordinate.getX() == roundedX &&
                            cartesianCoordinate.getY() == roundedY &&
                           cartesianCoordinate.getZ() == roundedZ)
                 .findAny();
            if (valueObject.isPresent()) {
                return valueObject.get();
            } else {
                CartesianCoordinate newValueObject = new CartesianCoordinate(roundedX, roundedY, roundedZ);
                cartesianCoordinates.add(newValueObject);
                return newValueObject;
            }
        }
    }

    private static double roundToDecimalPlaces(double toRound, int decimalPlaces) {
        double shiftingFactor = Math.pow(10, decimalPlaces);
        return Math.round(toRound * shiftingFactor) / shiftingFactor;
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
        Assertions.checkNotNull(rset, "The entered result set must not be null");
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
        double phi = Math.atan2(getY(), getX());
        double theta = Math.acos(getZ() / radius);

        SphericCoordinate resultingSphericCoordinate;

        // check if no division by zero occurred
        if (Double.isFinite(phi) && Double.isFinite(theta)) {
            resultingSphericCoordinate = SphericCoordinate.getValueObject(phi, theta, radius);
        } else {
            resultingSphericCoordinate = SphericCoordinate.getValueObject(0, 0, 0);
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
