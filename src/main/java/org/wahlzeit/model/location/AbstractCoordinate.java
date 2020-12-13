package org.wahlzeit.model.location;

import java.sql.*;
import java.util.Objects;

public abstract class AbstractCoordinate implements Coordinate {

    /**
     * The number of decimal places for the cartesian coordinate components x,y,z that are considered, when two
     * coordinates are checked to be equal or not.
     */
    private static final int DECIMAL_PLACES_FOR_EQUALITY = 3;

    protected abstract void assertClassInvariants();

    /**
     * Checks if the entered object is null.
     * @param object the object to check
     * @param message the error message for the exception if the object is null
     * @throws NullPointerException if the object is null
     */
    protected static void assertNotNull(Object object, String message) {
        if (object == null)
            throw new NullPointerException(message);
    }

    /**
     * Checks if the entered double is finite.
     * @param d the double to check
     * @param message the error message for the exception if the double is not finite
     * @throws IllegalArgumentException if the double is not finite
     */
    protected static void assertDoubleArgumentIsFinite(double d, String message) {
        if (!Double.isFinite(d))
            throw new IllegalArgumentException(message);
    }

    protected static void assertResultSetHasCoordinateColumns(ResultSet rset) throws SQLException {
        assertResultSetArgumentHasColumnAndType(rset, "coordinate_1", Types.DOUBLE);
        assertResultSetArgumentHasColumnAndType(rset, "coordinate_2", Types.DOUBLE);
        assertResultSetArgumentHasColumnAndType(rset, "coordinate_3", Types.DOUBLE);
    }

    /**
     * Checks if the entered result set has a column with a certain label and a certain type.
     * @param rset the result set to check
     * @param columnLabel the label that a column in the result set should have
     * @param sqlType the sql type that the column should have (choose a value of {@link Types})
     * @throws IllegalArgumentException if the result set does not have a column with the specified label or if the
     *                                  column with the specified label does not have the specified type
     * @throws SQLException if the database cannot be accessed or if the result set is closed
     */
    protected static void assertResultSetArgumentHasColumnAndType(ResultSet rset, String columnLabel, int sqlType)
            throws SQLException {

        ResultSetMetaData metaData = rset.getMetaData();

        int columnIndex = assertResultSetArgumentHasColumn(metaData, columnLabel,
                "The column '" + columnLabel + "' must exist");
        assertResultSetArgumentHasColumnType(metaData, columnIndex, sqlType,
                "The column '" + columnLabel + "' must be of type " + JDBCType.valueOf(sqlType).getName());
    }

    private static int assertResultSetArgumentHasColumn(ResultSetMetaData metaData, String columnLabel, String message) throws SQLException {
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); ++columnIndex) {
            if (metaData.getColumnLabel(columnIndex).equals(columnLabel))
                return columnIndex;
        }
        throw new IllegalArgumentException(message);
    }

    private static void assertResultSetArgumentHasColumnType(ResultSetMetaData metaData, int columnIndex, int sqlType, String message) throws SQLException {
        if (metaData.getColumnType(columnIndex) != sqlType)
            throw new IllegalArgumentException(message);
    }

    @Override
    public final void writeOn(ResultSet rset) throws SQLException {
        // invariant
        assertClassInvariants();

        // pre-condition
        assertResultSetArgumentHasColumnAndType(rset, "coordinate_type", Types.INTEGER);

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
        assertNotNull(otherCoordinate, "The entered coordinate for cartesian distance calculation must not be null");

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
        assertNotNull(otherCoordinate, "The entered coordinate for central angle calculation must not be null");

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
