package org.wahlzeit.model.location;

import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for the {@link CartesianCoordinate} class.
 */
public class CartesianCoordinateTest {

    @Test
    public void testDefaultConstructor() {
        // when
        CartesianCoordinate coordinate = new CartesianCoordinate();

        // then
        assertEquals(0.0, coordinate.getX(), 0.0);
        assertEquals(0.0, coordinate.getY(), 0.0);
        assertEquals(0.0, coordinate.getZ(), 0.0);

        assertEquals(new CartesianCoordinate(0.0, 0.0, 0.0), coordinate);
    }

    @Test
    public void testComponentConstructor() {
        // given
        double x = 0.42;
        double y = 123.456;
        double z = 3.1415;

        // when
        CartesianCoordinate coordinate = new CartesianCoordinate(x, y, z);

        // then
        assertEquals(x, coordinate.getX(), 0.0);
        assertEquals(y, coordinate.getY(), 0.0);
        assertEquals(z, coordinate.getZ(), 0.0);
    }

    @Test
    public void testResultSetConstructor() throws SQLException {
        // given
        double x = 9876.54321;
        double y = 0.000001;
        double z = 42.0;

        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(x);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(y);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(z);

        // when
        CartesianCoordinate coordinate = new CartesianCoordinate(mockedResultSet);

        // then
        assertEquals(x, coordinate.getX(), 0.0);
        assertEquals(y, coordinate.getY(), 0.0);
        assertEquals(z, coordinate.getZ(), 0.0);

        verify(mockedResultSet, times(1)).getDouble("coordinate_1");
        verify(mockedResultSet, times(1)).getDouble("coordinate_2");
        verify(mockedResultSet, times(1)).getDouble("coordinate_3");
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testResultSetConstructorThrowsNullPointerException() throws SQLException {
        // when
        new CartesianCoordinate(null);
    }

    @Test(expected = SQLException.class)
    public void testResultSetConstructorThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble(anyString())).thenThrow(new SQLException());

        // when
        new CartesianCoordinate(mockedResultSet);
    }

    @Test
    public void testWriteOn() throws SQLException {
        // given
        double x = 99.0;
        double y = 123.321;
        double z = 1e-10;
        CartesianCoordinate coordinate = new CartesianCoordinate(x, y, z);

        ResultSet mockedResultSet = mock(ResultSet.class);

        // when
        coordinate.writeOn(mockedResultSet);

        // then
        verify(mockedResultSet, times(1)).updateInt("coordinate_type", CoordinateType.Cartesian.ordinal());
        verify(mockedResultSet, times(1)).updateDouble("coordinate_1", x);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_2", y);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_3", z);
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        // given
        CartesianCoordinate coordinate = new CartesianCoordinate();

        // when
        coordinate.writeOn(null);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        // given
        CartesianCoordinate coordinate = new CartesianCoordinate();

        ResultSet mockedResultSet = mock(ResultSet.class);
        doThrow(new SQLException()).when(mockedResultSet).updateDouble(anyString(), anyDouble());

        // when
        coordinate.writeOn(mockedResultSet);
    }

    @Test
    public void testGetType() {
        // given
        CartesianCoordinate coordinate = new CartesianCoordinate();

        // when
        CoordinateType type = coordinate.getType();

        // then
        assertEquals(CoordinateType.Cartesian, type);
    }

    @Test
    public void testAsCartesianCoordinate() {
        // given
        CartesianCoordinate coordinate = new CartesianCoordinate();

        // when
        CartesianCoordinate otherCoordinate = coordinate.asCartesianCoordinate();

        // then
        assertEquals(coordinate, otherCoordinate);
    }

    @Test
    public void testAsSphericCoordinate() {
        // given
        CartesianCoordinate cartesianCoordinate = new CartesianCoordinate(1, -1, 1);

        // when
        SphericCoordinate sphericCoordinate = cartesianCoordinate.asSphericCoordinate();

        // then
        assertEquals(-Math.PI / 4, sphericCoordinate.getPhi(), 1e-12);
        assertEquals(Math.acos(1 / Math.sqrt(3)), sphericCoordinate.getTheta(), 1e-12);
        assertEquals(Math.sqrt(3), sphericCoordinate.getRadius(), 1e-12);
    }

    @Test
    public void testAsSphericCoordinatePreventsDivisionByZero() {
        // given
        CartesianCoordinate cartesianCoordinate = new CartesianCoordinate(0, 0, 0);

        // when
        SphericCoordinate sphericCoordinate = cartesianCoordinate.asSphericCoordinate();

        // then
        assertTrue(Double.isFinite(sphericCoordinate.getPhi()));
        assertTrue(Double.isFinite(sphericCoordinate.getTheta()));
        assertEquals(0, sphericCoordinate.getRadius(), 0.0);
    }

    @Test
    public void testExactEquality() {
        // given
        CartesianCoordinate firstCoordinate = new CartesianCoordinate(7.654321, 8.7654321, 9.87654321);
        CartesianCoordinate secondCoordinate = new CartesianCoordinate(7.654321, 8.7654321, 9.87654321);

        // then
        assertEquals(firstCoordinate, secondCoordinate);
        assertEquals(secondCoordinate, firstCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testJustEquality() {
        // given
        CartesianCoordinate firstCoordinate = new CartesianCoordinate(7.6543, 8.7654, 9.8765);
        CartesianCoordinate secondCoordinate = new CartesianCoordinate(7.6536, 8.7647, 9.8774);

        // then
        assertEquals(firstCoordinate, secondCoordinate);
        assertEquals(secondCoordinate, firstCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testInequality() {
        // given
        Coordinate firstCoordinate = new CartesianCoordinate(9.87654321, 8.7654321, 7.654321);
        CartesianCoordinate secondCoordinate = new CartesianCoordinate(7.654321, 8.7654321, 9.87654321);

        // then
        assertNotEquals(firstCoordinate, secondCoordinate);
        assertNotEquals(secondCoordinate, firstCoordinate);
    }

    @Test
    public void testAlmostEquality() {
        // given
        Coordinate firstCoordinate = new CartesianCoordinate(7.6543, 8.7654, 9.8765);
        CartesianCoordinate secondCoordinate = new CartesianCoordinate(7.6534, 8.7656, 9.8775);

        // then
        assertNotEquals(firstCoordinate, secondCoordinate);
        assertNotEquals(secondCoordinate, firstCoordinate);
    }

    @Test
    public void testInequalityWithWrongType() {
        // given
        Coordinate firstCoordinate = new CartesianCoordinate();
        Object object = new Object();

        // then
        assertNotEquals(object, firstCoordinate);
    }

    @Test
    public void testEqualityToSphericCoordinate() {
        // given
        CartesianCoordinate cartesianCoordinate = new CartesianCoordinate(0.004233, -100.234112, 5.876342);
        SphericCoordinate sphericCoordinate = cartesianCoordinate.asSphericCoordinate();

        // then
        assertTrue(cartesianCoordinate.isEqual(sphericCoordinate));
        assertTrue(sphericCoordinate.isEqual(cartesianCoordinate));

        assertEquals(cartesianCoordinate, sphericCoordinate);
        assertEquals(sphericCoordinate, cartesianCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(cartesianCoordinate.hashCode(), sphericCoordinate.hashCode());
    }

}
