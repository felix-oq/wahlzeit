package org.wahlzeit.model.location;

import org.wahlzeit.utils.Assertions;

import java.sql.*;
import java.util.Objects;

public abstract class AbstractCoordinate implements Coordinate {

    /**
     * The number of decimal places for the cartesian coordinate components x,y,z that are considered, when two
     * coordinates are checked to be equal or not.
     */
    private static final int DECIMAL_PLACES_FOR_EQUALITY = 3;

    protected abstract void assertClassInvariants();

    protected static void assertResultSetHasCoordinateColumns(ResultSet rset) throws SQLException {
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "coordinate_1", Types.DOUBLE);
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "coordinate_2", Types.DOUBLE);
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "coordinate_3", Types.DOUBLE);
    }

    @Override
    public final void writeOn(ResultSet rset) throws SQLException {
        // invariant
        assertClassInvariants();

        // pre-condition
        Assertions.checkResultSetArgumentHasColumnAndType(rset, "coordinate_type", Types.INTEGER);

        rset.updateInt("coordinate_type", getType().ordinal());
        doWriteOn(rset);

        // invariant
        assertClassInvariants();
    }

    protected abstract void doWriteOn(ResultSet rset) throws SQLException;

    @Override
    public final CoordinateType getType() {
        // invariant
        assertClassInvariants();

        CoordinateType result = doGetType();

        // post-condition
        assert result != null : "The resulting coordinate type must not be null";

        // invariant
        assertClassInvariants();

        return result;
    }

    protected abstract CoordinateType doGetType();

    @Override
    public double getCartesianDistance(Coordinate otherCoordinate) {
        // invariant
        assertClassInvariants();

        // pre-condition
        Assertions.checkNotNull(otherCoordinate, "The entered coordinate for cartesian distance calculation must not be null");

        CartesianCoordinate thisAsCartesianCoordinate = this.asCartesianCoordinate();
        CartesianCoordinate otherAsCartesianCoordinate = otherCoordinate.asCartesianCoordinate();

        double differenceInX = thisAsCartesianCoordinate.getX() - otherAsCartesianCoordinate.getX();
        double differenceInY = thisAsCartesianCoordinate.getY() - otherAsCartesianCoordinate.getY();
        double differenceInZ = thisAsCartesianCoordinate.getZ() - otherAsCartesianCoordinate.getZ();

        double cartesianDistance = Math.sqrt(differenceInX * differenceInX
                + differenceInY * differenceInY
                + differenceInZ * differenceInZ);

        // post-condition
        assert Double.isFinite(cartesianDistance) : "The resulting distance has to be finite";

        // invariant
        assertClassInvariants();

        return cartesianDistance;
    }

    @Override
    public double getCentralAngle(Coordinate otherCoordinate) {
        // invariant
        assertClassInvariants();

        // pre-condition
        Assertions.checkNotNull(otherCoordinate, "The entered coordinate for central angle calculation must not be null");

        SphericCoordinate thisAsSphericCoordinate = this.asSphericCoordinate();
        SphericCoordinate otherAsSphericCoordinate = otherCoordinate.asSphericCoordinate();

        double deltaLongitude = Math.abs(thisAsSphericCoordinate.getPhi() - otherAsSphericCoordinate.getPhi());

        double centralAngle = Math.acos(Math.sin(thisAsSphericCoordinate.getTheta()) * Math.sin(otherAsSphericCoordinate.getTheta())
                + Math.cos(thisAsSphericCoordinate.getTheta()) * Math.cos(otherAsSphericCoordinate.getTheta())
                * Math.cos(deltaLongitude));

        // post-condition
        assert Double.isFinite(centralAngle) : "The resulting angle has to be finite";
        assert 0 <= centralAngle && centralAngle <= Math.PI : "The resulting angle has to be between 0 and pi";

        // invariant
        assertClassInvariants();

        return centralAngle;
    }

    @Override
    public final boolean isEqual(Coordinate otherCoordinate) {
        // invariant
        assertClassInvariants();

        if (otherCoordinate == null) return false;

        CartesianCoordinate thisAsCartesianCoordinate = this.asCartesianCoordinate();
        CartesianCoordinate otherAsCartesianCoordinate = otherCoordinate.asCartesianCoordinate();

        double roundedX = roundToDecimalPlaces(thisAsCartesianCoordinate.getX(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedX = roundToDecimalPlaces(otherAsCartesianCoordinate.getX(), DECIMAL_PLACES_FOR_EQUALITY);

        double roundedY = roundToDecimalPlaces(thisAsCartesianCoordinate.getY(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedY = roundToDecimalPlaces(otherAsCartesianCoordinate.getY(), DECIMAL_PLACES_FOR_EQUALITY);

        double roundedZ = roundToDecimalPlaces(thisAsCartesianCoordinate.getZ(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedZ = roundToDecimalPlaces(otherAsCartesianCoordinate.getZ(), DECIMAL_PLACES_FOR_EQUALITY);

        boolean isEqual = roundedX == otherRoundedX &&
                roundedY == otherRoundedY &&
                roundedZ == otherRoundedZ;

        // invariant
        assertClassInvariants();

        return isEqual;
    }

    private double roundToDecimalPlaces(double toRound, int decimalPlaces) {
        double shiftingFactor = Math.pow(10, decimalPlaces);
        return Math.round(toRound * shiftingFactor) / shiftingFactor;
    }

    @Override
    public boolean equals(Object object) {
        // invariant
        assertClassInvariants();

        boolean equals = false;
        if (object instanceof Coordinate) {
            equals = this.isEqual((Coordinate) object);
        }

        // invariant
        assertClassInvariants();

        return equals;
    }

    @Override
    public int hashCode() {
        // invariant
        assertClassInvariants();

        CartesianCoordinate thisAsCartesianCoordinate = this.asCartesianCoordinate();

        // use the rounded x-, y-, and z-components for hash computation, otherwise objects that are equal would not
        // have the same hash code
        int hashCode = Objects.hash(
                roundToDecimalPlaces(thisAsCartesianCoordinate.getX(), DECIMAL_PLACES_FOR_EQUALITY),
                roundToDecimalPlaces(thisAsCartesianCoordinate.getY(), DECIMAL_PLACES_FOR_EQUALITY),
                roundToDecimalPlaces(thisAsCartesianCoordinate.getZ(), DECIMAL_PLACES_FOR_EQUALITY));

        // invariant
        assertClassInvariants();

        return hashCode;
    }
}
