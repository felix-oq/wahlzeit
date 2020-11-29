package org.wahlzeit.model.location;

import org.junit.Test;
import org.wahlzeit.model.location.Coordinate;
import org.wahlzeit.model.location.Location;

import java.sql.ResultSet;
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
        assertNotNull(location.coordinate);
        assertEquals(0.0, location.coordinate.getX(), 0.0);
        assertEquals(0.0, location.coordinate.getY(), 0.0);
        assertEquals(0.0, location.coordinate.getZ(), 0.0);
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
    public void testResultSetConstructor() throws SQLException {
        // given
        double x = 1234.56789;
        double y = -42;
        double z = -8e-5;

        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble("coordinate_x")).thenReturn(x);
        when(mockedResultSet.getDouble("coordinate_y")).thenReturn(y);
        when(mockedResultSet.getDouble("coordinate_z")).thenReturn(z);

        // when
        Location location = new Location(mockedResultSet);

        // then
        assertNotNull(location.coordinate);
        assertEquals(x, location.coordinate.getX(), 0.0);
        assertEquals(y, location.coordinate.getY(), 0.0);
        assertEquals(z, location.coordinate.getZ(), 0.0);

        verify(mockedResultSet, times(1)).getDouble("coordinate_x");
        verify(mockedResultSet, times(1)).getDouble("coordinate_y");
        verify(mockedResultSet, times(1)).getDouble("coordinate_z");
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
