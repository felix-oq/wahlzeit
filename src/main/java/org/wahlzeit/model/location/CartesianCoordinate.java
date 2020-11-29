package org.wahlzeit.model.location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * This class represents a point in a three dimensional space using cartesian coordinates.
 */
public class CartesianCoordinate implements Coordinate {

    private double x;
    private double y;
    private double z;

    /**
     * The number of decimal places for x,y,z that are considered, when two coordinates are checked to be equal or not.
     */
    private static final int DECIMAL_PLACES_FOR_EQUALITY = 3;

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
        rset.updateInt("coordinate_type", getType().ordinal());

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
    public double getCartesianDistance(Coordinate otherCoordinate) {
        CartesianCoordinate otherCartesianCoordinate = otherCoordinate.asCartesianCoordinate();

        double differenceInX = this.getX() - otherCartesianCoordinate.getX();
        double differenceInY = this.getY() - otherCartesianCoordinate.getY();
        double differenceInZ = this.getZ() - otherCartesianCoordinate.getZ();

        return Math.sqrt(differenceInX * differenceInX
                + differenceInY * differenceInY
                + differenceInZ * differenceInZ);
    }

    @Override
    public SphericCoordinate asSphericCoordinate() {
        double radius = Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
        double phi = Math.atan(getY() / getX());
        double theta = Math.acos(getZ() / radius);

        return new SphericCoordinate(phi, theta, radius);
    }

    @Override
    public double getCentralAngle(Coordinate otherCoordinate) {
        SphericCoordinate thisSphericCoordinate = this.asSphericCoordinate();
        return thisSphericCoordinate.getCentralAngle(otherCoordinate);
    }

    @Override
    public boolean isEqual(Coordinate otherCoordinate) {
        if (otherCoordinate == null) return false;

        CartesianCoordinate otherCartesianCoordinate = otherCoordinate.asCartesianCoordinate();

        double roundedX = roundToDecimalPlaces(getX(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedX = roundToDecimalPlaces(otherCartesianCoordinate.getX(), DECIMAL_PLACES_FOR_EQUALITY);

        double roundedY = roundToDecimalPlaces(getY(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedY = roundToDecimalPlaces(otherCartesianCoordinate.getY(), DECIMAL_PLACES_FOR_EQUALITY);

        double roundedZ = roundToDecimalPlaces(getZ(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedZ = roundToDecimalPlaces(otherCartesianCoordinate.getZ(), DECIMAL_PLACES_FOR_EQUALITY);

        return roundedX == otherRoundedX &&
                roundedY == otherRoundedY &&
                roundedZ == otherRoundedZ;
    }

    private double roundToDecimalPlaces(double toRound, int decimalPlaces) {
        double shiftingFactor = Math.pow(10, decimalPlaces);
        return Math.round(toRound * shiftingFactor) / shiftingFactor;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Coordinate) {
            return this.isEqual((Coordinate) object);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        // use the rounded coordinates for hash computation, otherwise objects that are equal would not have the same
        // hash code
        return Objects.hash(roundToDecimalPlaces(getX(), DECIMAL_PLACES_FOR_EQUALITY),
                roundToDecimalPlaces(getY(), DECIMAL_PLACES_FOR_EQUALITY),
                roundToDecimalPlaces(getZ(), DECIMAL_PLACES_FOR_EQUALITY));
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
