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
    public void testDefaultGetValueObject() {
        // when
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject();

        // then
        assertEquals(0.0, coordinate.getX(), 0.0);
        assertEquals(0.0, coordinate.getY(), 0.0);
        assertEquals(0.0, coordinate.getZ(), 0.0);

        assertEquals(CartesianCoordinate.getValueObject(0.0, 0.0, 0.0), coordinate);
        assertSame(CartesianCoordinate.getValueObject(0.0, 0.0, 0.0), coordinate);
    }

    @Test
    public void testComponentGetValueObject() {
        // given
        double x = 0.42;
        double y = 123.456;
        double z = 3.1415;

        // when
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject(x, y, z);

        // then
        assertEquals(x, coordinate.getX(), 0.001);
        assertEquals(y, coordinate.getY(), 0.001);
        assertEquals(z, coordinate.getZ(), 0.001);

        assertSame(CartesianCoordinate.getValueObject(x, y, z), coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComponentGetValueObjectThrowsIllegalArgumentException() {
        // when
        CartesianCoordinate.getValueObject(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    @Test
    public void testResultSetGetValueObject() throws SQLException {
        // given
        double x = 9876.54321;
        double y = 0.000001;
        double z = 42.0;

        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(x);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(y);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(z);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject(mockedResultSet);

        // then
        assertEquals(x, coordinate.getX(), 0.001);
        assertEquals(y, coordinate.getY(), 0.001);
        assertEquals(z, coordinate.getZ(), 0.001);

        verify(mockedResultSet, times(1)).getDouble("coordinate_1");
        verify(mockedResultSet, times(1)).getDouble("coordinate_2");
        verify(mockedResultSet, times(1)).getDouble("coordinate_3");
        verify(mockedResultSet, atLeastOnce()).getMetaData();
        verifyNoMoreInteractions(mockedResultSet);

        assertSame(CartesianCoordinate.getValueObject(mockedResultSet), coordinate);
    }

    @Test(expected = NullPointerException.class)
    public void testResultSetGetValueObjectThrowsNullPointerException() throws SQLException {
        // when
        CartesianCoordinate.getValueObject(null);
    }

    @Test(expected = SQLException.class)
    public void testResultSetGetValueObjectThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble(anyString())).thenThrow(new SQLException());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        CartesianCoordinate.getValueObject(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResultSetGetValueObjectThrowsIllegalArgumentException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        CartesianCoordinate.getValueObject(mockedResultSet);
    }

    @Test
    public void testWriteOn() throws SQLException {
        // given
        double x = 99.0;
        double y = 123.321;
        double z = 0.01;
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject(x, y, z);

        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(x);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(y);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(z);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        coordinate.writeOn(mockedResultSet);

        // then
        verify(mockedResultSet, times(1)).updateInt("coordinate_type", CoordinateType.Cartesian.ordinal());
        verify(mockedResultSet, times(1)).updateDouble("coordinate_1", x);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_2", y);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_3", z);
        verify(mockedResultSet, atLeastOnce()).getMetaData();
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        // given
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject();

        // when
        coordinate.writeOn(null);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        // given
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject();

        ResultSet mockedResultSet = mock(ResultSet.class);
        doThrow(new SQLException()).when(mockedResultSet).updateDouble(anyString(), anyDouble());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        coordinate.writeOn(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteOnThrowsIllegalArgumentException() throws SQLException {
        // given
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject();

        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        coordinate.writeOn(mockedResultSet);
    }

    @Test
    public void testGetType() {
        // given
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject();

        // when
        CoordinateType type = coordinate.getType();

        // then
        assertEquals(CoordinateType.Cartesian, type);
    }

    @Test
    public void testAsCartesianCoordinate() {
        // given
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject();

        // when
        CartesianCoordinate otherCoordinate = coordinate.asCartesianCoordinate();

        // then
        assertEquals(coordinate, otherCoordinate);
    }

    @Test
    public void testAsSphericCoordinate() {
        // given
        CartesianCoordinate cartesianCoordinate = CartesianCoordinate.getValueObject(1, -1, 1);

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
        CartesianCoordinate cartesianCoordinate = CartesianCoordinate.getValueObject(0, 0, 0);

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
        CartesianCoordinate firstCoordinate = CartesianCoordinate.getValueObject(7.654321, 8.7654321, 9.87654321);
        CartesianCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.654321, 8.7654321, 9.87654321);

        // then
        assertEquals(firstCoordinate, secondCoordinate);
        assertEquals(secondCoordinate, firstCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testJustEquality() {
        // given
        CartesianCoordinate firstCoordinate = CartesianCoordinate.getValueObject(7.6543, 8.7654, 9.8765);
        CartesianCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.6536, 8.7647, 9.8774);

        // then
        assertEquals(firstCoordinate, secondCoordinate);
        assertEquals(secondCoordinate, firstCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testInequality() {
        // given
        Coordinate firstCoordinate = CartesianCoordinate.getValueObject(9.87654321, 8.7654321, 7.654321);
        CartesianCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.654321, 8.7654321, 9.87654321);

        // then
        assertNotEquals(firstCoordinate, secondCoordinate);
        assertNotEquals(secondCoordinate, firstCoordinate);
    }

    @Test
    public void testAlmostEquality() {
        // given
        Coordinate firstCoordinate = CartesianCoordinate.getValueObject(7.6543, 8.7654, 9.8765);
        CartesianCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.6534, 8.7656, 9.8775);

        // then
        assertNotEquals(firstCoordinate, secondCoordinate);
        assertNotEquals(secondCoordinate, firstCoordinate);
    }

    @Test
    public void testInequalityWithWrongType() {
        // given
        Coordinate firstCoordinate = CartesianCoordinate.getValueObject();
        Object object = new Object();

        // then
        assertNotEquals(object, firstCoordinate);
    }

    @Test
    public void testEqualityToSphericCoordinate() {
        // given
        CartesianCoordinate cartesianCoordinate = CartesianCoordinate.getValueObject(0.004233, -100.234112, 5.876342);
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
