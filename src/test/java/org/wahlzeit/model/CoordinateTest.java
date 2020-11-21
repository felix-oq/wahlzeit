package org.wahlzeit.model;

import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for the {@link Coordinate} class.
 */
public class CoordinateTest {

    @Test
    public void testDefaultConstructor() {
        // when
        Coordinate coordinate = new Coordinate();

        // then
        assertEquals(0.0, coordinate.getX(), 0.0);
        assertEquals(0.0, coordinate.getY(), 0.0);
        assertEquals(0.0, coordinate.getZ(), 0.0);

        assertEquals(new Coordinate(0.0, 0.0, 0.0), coordinate);
    }

    @Test
    public void testComponentConstructor() {
        // given
        double x = 0.42;
        double y = 123.456;
        double z = 3.1415;

        // when
        Coordinate coordinate = new Coordinate(x, y, z);

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
        when(mockedResultSet.getDouble("coordinate_x")).thenReturn(x);
        when(mockedResultSet.getDouble("coordinate_y")).thenReturn(y);
        when(mockedResultSet.getDouble("coordinate_z")).thenReturn(z);

        // when
        Coordinate coordinate = new Coordinate(mockedResultSet);

        // then
        assertEquals(x, coordinate.getX(), 0.0);
        assertEquals(y, coordinate.getY(), 0.0);
        assertEquals(z, coordinate.getZ(), 0.0);

        verify(mockedResultSet, times(1)).getDouble("coordinate_x");
        verify(mockedResultSet, times(1)).getDouble("coordinate_y");
        verify(mockedResultSet, times(1)).getDouble("coordinate_z");
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testResultSetConstructorThrowsNullPointerException() throws SQLException {
        // when
        new Coordinate(null);
    }

    @Test(expected = SQLException.class)
    public void testResultSetConstructorThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble(anyString())).thenThrow(new SQLException());

        // when
        new Coordinate(mockedResultSet);
    }

    @Test
    public void testWriteOn() throws SQLException {
        // given
        double x = 99.0;
        double y = 123.321;
        double z = 1e-10;
        Coordinate coordinate = new Coordinate(x, y, z);

        ResultSet mockedResultSet = mock(ResultSet.class);

        // when
        coordinate.writeOn(mockedResultSet);

        // then
        verify(mockedResultSet, times(1)).updateDouble("coordinate_x", x);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_y", y);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_z", z);
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        // given
        Coordinate coordinate = new Coordinate();

        // when
        coordinate.writeOn(null);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        // given
        Coordinate coordinate = new Coordinate();

        ResultSet mockedResultSet = mock(ResultSet.class);
        doThrow(new SQLException()).when(mockedResultSet).updateDouble(anyString(), anyDouble());

        // when
        coordinate.writeOn(mockedResultSet);
    }

    @Test
    public void testGetDifference() {
        // given
        Coordinate firstCoordinate = new Coordinate(-0.562, 101.35, -121.421);
        Coordinate secondCoordinate = new Coordinate(0.438, -14.1343542, -5.0);

        // when
        double distanceFirstToSecond = firstCoordinate.getDistance(secondCoordinate);
        double distanceSecondToFirst = secondCoordinate.getDistance(firstCoordinate);

        // then
        assertEquals(distanceFirstToSecond, distanceSecondToFirst, 0.0);
        assertEquals(163.9862351113381, distanceFirstToSecond, 1e-12);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDifferenceThrowsNullPointerException() {
        // given
        Coordinate coordinate = new Coordinate();

        // when
        coordinate.getDistance(null);
    }

    @Test
    public void testExactEquality() {
        // given
        Coordinate firstCoordinate = new Coordinate(7.654321, 8.7654321, 9.87654321);
        Coordinate secondCoordinate = new Coordinate(7.654321, 8.7654321, 9.87654321);

        // then
        assertTrue(firstCoordinate.isEqual(secondCoordinate));
        assertEquals(firstCoordinate, secondCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testJustEquality() {
        // given
        Coordinate firstCoordinate = new Coordinate(7.6543, 8.7654, 9.8765);
        Coordinate secondCoordinate = new Coordinate(7.6536, 8.7647, 9.8774);

        // then
        assertTrue(firstCoordinate.isEqual(secondCoordinate));
        assertEquals(firstCoordinate, secondCoordinate);

        // equal coordinates have to deliver the same hash code
        assertEquals(firstCoordinate.hashCode(), secondCoordinate.hashCode());
    }

    @Test
    public void testInequality() {
        // given
        Coordinate firstCoordinate = new Coordinate(9.87654321, 8.7654321, 7.654321);
        Coordinate secondCoordinate = new Coordinate(7.654321, 8.7654321, 9.87654321);

        // then
        assertFalse(firstCoordinate.isEqual(secondCoordinate));
        assertNotEquals(firstCoordinate, secondCoordinate);
    }

    @Test
    public void testAlmostEquality() {
        // given
        Coordinate firstCoordinate = new Coordinate(7.6543, 8.7654, 9.8765);
        Coordinate secondCoordinate = new Coordinate(7.6534, 8.7656, 9.8775);

        // then
        assertFalse(firstCoordinate.isEqual(secondCoordinate));
        assertNotEquals(firstCoordinate, secondCoordinate);
    }

    @Test
    public void testInequalityWithWrongType() {
        // given
        Coordinate firstCoordinate = new Coordinate();
        Object object = new Object();

        // then
        assertNotEquals(firstCoordinate, object);
    }

}
