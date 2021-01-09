package org.wahlzeit.model.location;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for the {@link SphericCoordinate} class.
 */
public class SphericCoordinateTest {

    @Test
    public void testDefaultGetValueObject() {
        // when
        SphericCoordinate coordinate = SphericCoordinate.getValueObject();

        // then
        assertEquals(0.0, coordinate.getPhi(), 0.0);
        assertEquals(0.0, coordinate.getTheta(), 0.0);
        assertEquals(0.0, coordinate.getRadius(), 0.0);

        assertEquals(SphericCoordinate.getValueObject(0.0, 0.0, 0.0), coordinate);
        assertSame(SphericCoordinate.getValueObject(0.0, 0.0, 0.0), coordinate);
    }

    @Test
    public void testComponentGetValueObject() {
        // given
        double phi = -42.24;
        double theta = 654.321;
        double radius = 2.7183;

        // when
        SphericCoordinate coordinate = SphericCoordinate.getValueObject(phi, theta, radius);

        // then
        assertEquals(phi, coordinate.getPhi(), 0.0);
        assertEquals(theta, coordinate.getTheta(), 0.0);
        assertEquals(radius, coordinate.getRadius(), 0.0);

        assertSame(SphericCoordinate.getValueObject(phi, theta, radius), coordinate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComponentGetValueObjectWithNonFiniteParametersThrowsIllegalArgumentException() {
        // when
        SphericCoordinate.getValueObject(Double.NEGATIVE_INFINITY, Double.NaN, Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComponentGetValueObjectWithNegativeRadiusThrowsIllegalArgumentException() {
        // when
        SphericCoordinate.getValueObject(-Math.PI, -0.5 * Math.PI, -1e-12);
    }

    @Test
    public void testResultSetGetValueObject() throws SQLException {
        // given
        double phi = 9876.54321;
        double theta = 0.000001;
        double radius = 42.0;

        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(phi);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(theta);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(radius);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        SphericCoordinate coordinate = SphericCoordinate.getValueObject(mockedResultSet);

        // then
        assertEquals(phi, coordinate.getPhi(), 0.0);
        assertEquals(theta, coordinate.getTheta(), 0.0);
        assertEquals(radius, coordinate.getRadius(), 0.0);

        verify(mockedResultSet, times(1)).getDouble("coordinate_1");
        verify(mockedResultSet, times(1)).getDouble("coordinate_2");
        verify(mockedResultSet, times(1)).getDouble("coordinate_3");
        verify(mockedResultSet, atLeastOnce()).getMetaData();
        verifyNoMoreInteractions(mockedResultSet);

        assertSame(SphericCoordinate.getValueObject(mockedResultSet), coordinate);
    }

    @Test(expected = NullPointerException.class)
    public void testResultSetGetValueObjectThrowsNullPointerException() throws SQLException {
        // when
        SphericCoordinate.getValueObject(null);
    }

    @Test(expected = SQLException.class)
    public void testResultSetGetValueObjectThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble(anyString())).thenThrow(new SQLException());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        SphericCoordinate.getValueObject(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResultSetGetValueObjectWithInvalidResultSetThrowsIllegalArgumentException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        SphericCoordinate.getValueObject(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResultSetGetValueObjectWithNegativeRadiusThrowsIllegalArgumentException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(-2*Math.PI);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(-Math.PI);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(-1e-12);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        SphericCoordinate.getValueObject(mockedResultSet);
    }

    @Test
    public void testWriteOn() throws SQLException {
        // given
        double phi = 99.0;
        double theta = 123.321;
        double radius = 1e-10;
        SphericCoordinate coordinate = SphericCoordinate.getValueObject(phi, theta, radius);

        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(phi);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(theta);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(radius);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        coordinate.writeOn(mockedResultSet);

        // then
        verify(mockedResultSet, times(1)).updateInt("coordinate_type", CoordinateType.Spheric.ordinal());
        verify(mockedResultSet, times(1)).updateDouble("coordinate_1", phi);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_2", theta);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_3", radius);
        verify(mockedResultSet, atLeastOnce()).getMetaData();
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        // given
        SphericCoordinate coordinate = SphericCoordinate.getValueObject();

        // when
        coordinate.writeOn(null);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        // given
        SphericCoordinate coordinate = SphericCoordinate.getValueObject();

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
        SphericCoordinate coordinate = SphericCoordinate.getValueObject();

        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        coordinate.writeOn(mockedResultSet);
    }

    @Test
    public void testGetType() {
        // given
        SphericCoordinate coordinate = SphericCoordinate.getValueObject();

        // when
        CoordinateType type = coordinate.getType();

        // then
        assertEquals(CoordinateType.Spheric, type);
    }

    @Test
    public void testAsCartesianCoordinate() {
        // given
        SphericCoordinate sphericCoordinate = SphericCoordinate.getValueObject(Math.PI / 4, -Math.acos(1 / Math.sqrt(3)), Math.sqrt(3));

        // when
        CartesianCoordinate cartesianCoordinate = sphericCoordinate.asCartesianCoordinate();

        // then
        assertEquals(-1, cartesianCoordinate.getX(), 1e-12);
        assertEquals(-1, cartesianCoordinate.getY(), 1e-12);
        assertEquals(1, cartesianCoordinate.getZ(), 1e-12);
    }

    @Test
    public void testAsSphericCoordinate() {
        // given
        SphericCoordinate coordinate = SphericCoordinate.getValueObject();

        // when
        SphericCoordinate otherCoordinate = coordinate.asSphericCoordinate();

        // then
        assertEquals(coordinate, otherCoordinate);
    }

    @Test
    public void testExactEquality() {
        // given
        SphericCoordinate firstCoordinate = CartesianCoordinate.getValueObject(7.654321, 8.7654321, 9.87654321).asSphericCoordinate();
        SphericCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.654321, 8.7654321, 9.87654321).asSphericCoordinate();

        // then
        assertEquals(firstCoordinate, secondCoordinate);
        assertEquals(secondCoordinate, firstCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testJustEquality() {
        // given
        SphericCoordinate firstCoordinate = CartesianCoordinate.getValueObject(7.6543, 8.7654, 9.8765).asSphericCoordinate();
        SphericCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.6536, 8.7647, 9.8774).asSphericCoordinate();

        // then
        assertEquals(firstCoordinate, secondCoordinate);
        assertEquals(secondCoordinate, firstCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testInequality() {
        // given
        SphericCoordinate firstCoordinate = CartesianCoordinate.getValueObject(9.87654321, 8.7654321, 7.654321).asSphericCoordinate();
        SphericCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.654321, 8.7654321, 9.87654321).asSphericCoordinate();

        // then
        assertNotEquals(firstCoordinate, secondCoordinate);
        assertNotEquals(secondCoordinate, firstCoordinate);
    }

    @Test
    public void testAlmostEquality() {
        // given
        SphericCoordinate firstCoordinate = CartesianCoordinate.getValueObject(7.6543, 8.7654, 9.8765).asSphericCoordinate();
        SphericCoordinate secondCoordinate = CartesianCoordinate.getValueObject(7.6534, 8.7656, 9.8775).asSphericCoordinate();

        // then
        assertNotEquals(firstCoordinate, secondCoordinate);
        assertNotEquals(secondCoordinate, firstCoordinate);
    }

    @Test
    public void testInequalityWithWrongType() {
        // given
        SphericCoordinate firstCoordinate = SphericCoordinate.getValueObject();
        Object object = new Object();

        // then
        assertNotEquals(object, firstCoordinate);
    }

    @Test
    public void testEqualityToCartesianCoordinate() {
        // given
        SphericCoordinate sphericCoordinate = SphericCoordinate.getValueObject(Math.PI, -1.2341, 17.3134);
        CartesianCoordinate cartesianCoordinate = sphericCoordinate.asCartesianCoordinate();

        // then
        assertTrue(sphericCoordinate.isEqual(cartesianCoordinate));
        assertTrue(cartesianCoordinate.isEqual(sphericCoordinate));

        assertEquals(sphericCoordinate, cartesianCoordinate);
        assertEquals(cartesianCoordinate, sphericCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(sphericCoordinate.hashCode(), cartesianCoordinate.hashCode());
    }

}
