package org.wahlzeit.model.location;

import org.wahlzeit.utils.Assertions;
import org.wahlzeit.utils.patterns.Pattern;
import org.wahlzeit.utils.patterns.PatternInstance;
import org.wahlzeit.utils.patterns.PatternParticipant;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;

/**
 * This class represents a point in a three dimensional space using spheric coordinates.
 */
@PatternInstance(pattern = Pattern.TemplateMethod, role = PatternParticipant.ConcreteClass)
public class SphericCoordinate extends AbstractCoordinate {

    private final double phi;
    private final double theta;
    private final double radius;

    private static final HashSet<SphericCoordinate> sphericCoordinates = new HashSet<>();

    /**
     * Gets a default spheric coordinate instance where its phi-, theta- and radius-components are initialized with
     * zero. The resulting coordinate is the corresponding value object for the given coordinate components.
     * @methodtype constructor
     */
    public static SphericCoordinate getValueObject() {
        return lookupValueObject(0.d, 0.d, 0.d);
    }

    /**
     * Gets a spheric coordinate instance using the provided arguments as phi-, theta- and radius-components. The
     * resulting coordinate is the corresponding value object for the given coordinate components.
     * @param phi the phi-component in radians
     * @param theta the theta-component in radians
     * @param radius the radius-component in radians
     * @throws IllegalArgumentException if any of the arguments is not finite or if the radius is negative
     * @methodtype constructor
     */
    public static SphericCoordinate getValueObject(double phi, double theta, double radius) {
        // pre-condition
        Assertions.checkDoubleArgumentIsFinite(phi, "The entered phi-component must be finite");
        Assertions.checkDoubleArgumentIsFinite(theta, "The entered theta-component must be finite");
        Assertions.checkDoubleArgumentIsFinite(radius, "The entered radius-component must be finite");
        if (radius < 0) throw new IllegalArgumentException("The entered radius-component must be non-negative");

        return lookupValueObject(phi, theta, radius);
    }

    /**
     * Gets a spheric coordinate instance by reading the first, second and third coordinate-components from the
     * provided result set and interpreting them as phi, theta and radius. The resulting coordinate is the corresponding
     * value object for the given coordinate components.
     * @param rset the result set to read the components from
     * @throws SQLException if the necessary values cannot be retrieved from the provided result set
     * @throws IllegalArgumentException if the provided result set does not have the necessary columns with their
     *                                  respective types or if the value for the radius is negative
     * @throws NullPointerException if the provided argument is null
     * @methodtype constructor
     */
    public static SphericCoordinate getValueObject(ResultSet rset) throws SQLException {
        // pre-condition
        Assertions.checkNotNull(rset, "The entered result set must not be null");
        assertResultSetHasCoordinateColumns(rset);

        double phi = rset.getDouble("coordinate_1");
        double theta = rset.getDouble("coordinate_2");
        double radius = rset.getDouble("coordinate_3");

        if (radius < 0)
            throw new IllegalArgumentException("The value for the radius-component must not be negative");

        return lookupValueObject(phi, theta, radius);
    }

    private static SphericCoordinate lookupValueObject(double phi, double theta, double radius) {

        synchronized(sphericCoordinates) {
            Optional<SphericCoordinate> valueObject = sphericCoordinates.stream()
                    .filter(sphericCoordinate ->
                            sphericCoordinate.getPhi() == phi &&
                                    sphericCoordinate.getTheta() == theta &&
                                    sphericCoordinate.getRadius() == radius)
                    .findAny();
            if (valueObject.isPresent()) {
                return valueObject.get();
            } else {
                SphericCoordinate newValueObject = new SphericCoordinate(phi, theta, radius);
                sphericCoordinates.add(newValueObject);
                return newValueObject;
            }
        }
    }

    private SphericCoordinate(double phi, double theta, double radius) {
        this.phi = phi;
        this.theta = theta;
        this.radius = radius;

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

        CartesianCoordinate resultingCartesianCoordinate = CartesianCoordinate.getValueObject(x, y, z);

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
