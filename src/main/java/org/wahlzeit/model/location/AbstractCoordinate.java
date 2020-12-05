package org.wahlzeit.model.location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class AbstractCoordinate implements Coordinate {

    /**
     * The number of decimal places for the cartesian coordinate components x,y,z that are considered, when two
     * coordinates are checked to be equal or not.
     */
    private static final int DECIMAL_PLACES_FOR_EQUALITY = 3;

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("coordinate_type", getType().ordinal());
    }

    @Override
    public double getCartesianDistance(Coordinate otherCoordinate) {
        CartesianCoordinate thisAsCartesianCoordinate = this.asCartesianCoordinate();
        CartesianCoordinate otherAsCartesianCoordinate = otherCoordinate.asCartesianCoordinate();

        double differenceInX = thisAsCartesianCoordinate.getX() - otherAsCartesianCoordinate.getX();
        double differenceInY = thisAsCartesianCoordinate.getY() - otherAsCartesianCoordinate.getY();
        double differenceInZ = thisAsCartesianCoordinate.getZ() - otherAsCartesianCoordinate.getZ();

        return Math.sqrt(differenceInX * differenceInX
                + differenceInY * differenceInY
                + differenceInZ * differenceInZ);
        }

    @Override
    public double getCentralAngle(Coordinate otherCoordinate) {
        SphericCoordinate thisAsSphericCoordinate = this.asSphericCoordinate();
        SphericCoordinate otherAsSphericCoordinate = otherCoordinate.asSphericCoordinate();

        double deltaLongitude = Math.abs(thisAsSphericCoordinate.getPhi() - otherAsSphericCoordinate.getPhi());

        return Math.acos(Math.sin(thisAsSphericCoordinate.getTheta()) * Math.sin(otherAsSphericCoordinate.getTheta())
                + Math.cos(thisAsSphericCoordinate.getTheta()) * Math.cos(otherAsSphericCoordinate.getTheta())
                * Math.cos(deltaLongitude));
    }

    @Override
    public final boolean isEqual(Coordinate otherCoordinate) {
        if (otherCoordinate == null) return false;

        CartesianCoordinate thisAsCartesianCoordinate = this.asCartesianCoordinate();
        CartesianCoordinate otherAsCartesianCoordinate = otherCoordinate.asCartesianCoordinate();

        double roundedX = roundToDecimalPlaces(thisAsCartesianCoordinate.getX(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedX = roundToDecimalPlaces(otherAsCartesianCoordinate.getX(), DECIMAL_PLACES_FOR_EQUALITY);

        double roundedY = roundToDecimalPlaces(thisAsCartesianCoordinate.getY(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedY = roundToDecimalPlaces(otherAsCartesianCoordinate.getY(), DECIMAL_PLACES_FOR_EQUALITY);

        double roundedZ = roundToDecimalPlaces(thisAsCartesianCoordinate.getZ(), DECIMAL_PLACES_FOR_EQUALITY);
        double otherRoundedZ = roundToDecimalPlaces(otherAsCartesianCoordinate.getZ(), DECIMAL_PLACES_FOR_EQUALITY);

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
        CartesianCoordinate thisAsCartesianCoordinate = this.asCartesianCoordinate();

        // use the rounded x-, y-, and z-components for hash computation, otherwise objects that are equal would not
        // have the same hash code
        return Objects.hash(
                roundToDecimalPlaces(thisAsCartesianCoordinate.getX(), DECIMAL_PLACES_FOR_EQUALITY),
                roundToDecimalPlaces(thisAsCartesianCoordinate.getY(), DECIMAL_PLACES_FOR_EQUALITY),
                roundToDecimalPlaces(thisAsCartesianCoordinate.getZ(), DECIMAL_PLACES_FOR_EQUALITY));
    }
}
