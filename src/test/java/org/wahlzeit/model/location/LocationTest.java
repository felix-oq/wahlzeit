package org.wahlzeit.model.location;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * Test cases for the {@link Location} class.
 */
public class LocationTest {

    @Test
    public void testDefaultConstructor() {
        // when
        Location location = new Location();

        // then
        assertTrue(location.coordinate instanceof CartesianCoordinate);
        CartesianCoordinate cartesianCoordinate = (CartesianCoordinate) location.coordinate;

        assertEquals(0.0, cartesianCoordinate.getX(), 0.0);
        assertEquals(0.0, cartesianCoordinate.getY(), 0.0);
        assertEquals(0.0, cartesianCoordinate.getZ(), 0.0);
    }

    @Test
    public void testCoordinateConstructor() {
        // given
        Coordinate coordinate = mock(Coordinate.class);

        // when
        Location location = new Location(coordinate);

        // then
        assertSame(coordinate, location.coordinate);
        verifyNoMoreInteractions(coordinate);
    }

    @Test
    public void testCoordinateConstructorWithNullParameter() {
        // given
        Coordinate coordinate = null;

        // when
        Location location = new Location(coordinate);

        // then
        assertNull(location.coordinate);
    }

    @Test
    public void testResultSetConstructorWithCartesianCoordinate() throws SQLException {
        // given
        double x = 1234.56789;
        double y = -42;
        double z = -8e-5;

        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getInt("coordinate_type")).thenReturn(CoordinateType.Cartesian.ordinal());
        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(x);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(y);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(z);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        Location location = new Location(mockedResultSet);

        // then
        assertTrue(location.coordinate instanceof CartesianCoordinate);
        CartesianCoordinate cartesianCoordinate = (CartesianCoordinate) location.coordinate;

        assertEquals(x, cartesianCoordinate.getX(), 0.0);
        assertEquals(y, cartesianCoordinate.getY(), 0.0);
        assertEquals(z, cartesianCoordinate.getZ(), 0.0);

        verify(mockedResultSet, times(1)).getInt("coordinate_type");
        verify(mockedResultSet, times(1)).getDouble("coordinate_1");
        verify(mockedResultSet, times(1)).getDouble("coordinate_2");
        verify(mockedResultSet, times(1)).getDouble("coordinate_3");
        verify(mockedResultSet, atLeastOnce()).getMetaData();
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test
    public void testResultSetConstructorWithSphericCoordinate() throws SQLException {
        // given
        double phi = 6e-5;
        double theta = 2 * Math.PI;
        double radius = 1080.0801;


        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getInt("coordinate_type")).thenReturn(CoordinateType.Spheric.ordinal());
        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(phi);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(theta);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(radius);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        Location location = new Location(mockedResultSet);

        // then
        assertTrue(location.coordinate instanceof SphericCoordinate);
        SphericCoordinate sphericCoordinate = (SphericCoordinate) location.coordinate;

        assertEquals(phi, sphericCoordinate.getPhi(), 0.0);
        assertEquals(theta, sphericCoordinate.getTheta(), 0.0);
        assertEquals(radius, sphericCoordinate.getRadius(), 0.0);

        verify(mockedResultSet, atLeastOnce()).getInt("coordinate_type");
        verify(mockedResultSet, atLeastOnce()).getDouble("coordinate_1");
        verify(mockedResultSet, atLeastOnce()).getDouble("coordinate_2");
        verify(mockedResultSet, atLeastOnce()).getDouble("coordinate_3");
        verify(mockedResultSet, atLeastOnce()).getMetaData();
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testResultSetConstructorThrowsNullPointerException() throws SQLException {
        // given
        ResultSet resultSet = null;

        // when
        new Location(resultSet);
    }

    @Test(expected = SQLException.class)
    public void testResultSetConstructorThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble(anyString())).thenThrow(new SQLException());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new Location(mockedResultSet);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testResultSetConstructorThrowsIndexOutOfBoundsException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getInt("coordinate_type")).thenReturn(-1);
        when(mockedResultSet.getDouble("coordinate_x")).thenReturn(1.423);
        when(mockedResultSet.getDouble("coordinate_y")).thenReturn(1023.123);
        when(mockedResultSet.getDouble("coordinate_z")).thenReturn(-0.023);

        // when
        new Location(mockedResultSet);
    }

    @Test
    public void testWriteOn() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        Coordinate mockedCoordinate = mock(Coordinate.class);

        Location location = new Location(mockedCoordinate);

        // when
        location.writeOn(mockedResultSet);

        // then
        verify(mockedCoordinate, times(1)).writeOn(mockedResultSet);
        verifyNoMoreInteractions(mockedCoordinate);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        // given
        Location location = new Location();

        // when
        location.writeOn(null);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);

        Coordinate mockedCoordinate = mock(Coordinate.class);
        doThrow(new SQLException()).when(mockedCoordinate).writeOn(mockedResultSet);

        Location location = new Location(mockedCoordinate);

        // when
        location.writeOn(mockedResultSet);
    }
}
