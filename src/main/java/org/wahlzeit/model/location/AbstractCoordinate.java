package org.wahlzeit.model.location;

import org.wahlzeit.utils.Assertions;
import org.wahlzeit.utils.patterns.Pattern;
import org.wahlzeit.utils.patterns.PatternInstance;
import org.wahlzeit.utils.patterns.PatternParticipant;

import java.sql.*;
import java.util.Objects;

@PatternInstance(pattern = Pattern.TemplateMethod, role = PatternParticipant.AbstractClass)
public abstract class AbstractCoordinate implements Coordinate {

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

        // since coordinates are value objects now, compare the object pointers for equality
        // the coordinate representation must be the same for both objects though
        boolean isEqual = (thisAsCartesianCoordinate == otherAsCartesianCoordinate);

        // invariant
        assertClassInvariants();

        return isEqual;
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

        // always compute the hashcode from cartesian coordinate representation for consistency among different coordinate representations
        CartesianCoordinate thisAsCartesianCoordinate = this.asCartesianCoordinate();
        int hashCode = Objects.hash(thisAsCartesianCoordinate.getX(),
                thisAsCartesianCoordinate.getY(),
                thisAsCartesianCoordinate.getZ());

        // invariant
        assertClassInvariants();

        return hashCode;
    }
}
