package org.wahlzeit.model.location;

import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

public class AbstractCoordinateTest {

    @Test
    public void testWriteOn() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);

        CoordinateType coordinateType = CoordinateType.Spheric;

        AbstractCoordinate mockedAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        when(mockedAbstractCoordinate.getType()).thenReturn(coordinateType);

        // when
        mockedAbstractCoordinate.writeOn(mockedResultSet);

        // then
        verify(mockedResultSet, times(1)).updateInt("coordinate_type", coordinateType.ordinal());
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        // given
        AbstractCoordinate mockedAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        // when
        mockedAbstractCoordinate.writeOn(null);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        // given
        CoordinateType coordinateType = CoordinateType.Spheric;

        AbstractCoordinate mockedAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        when(mockedAbstractCoordinate.getType()).thenReturn(coordinateType);

        ResultSet mockedResultSet = mock(ResultSet.class);
        doThrow(new SQLException()).when(mockedResultSet).updateInt(anyString(), anyInt());

        // when
        mockedAbstractCoordinate.writeOn(mockedResultSet);
    }

    @Test
    public void testGetCartesianDistance() {
        // given
        AbstractCoordinate mockedFirstAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        AbstractCoordinate mockedSecondAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        when(mockedFirstAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(-0.562, 101.35, -121.421));
        when(mockedSecondAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(0.438, -14.1343542, -5.0));

        // when
        double distanceFirstToSecond = mockedFirstAbstractCoordinate.getCartesianDistance(mockedSecondAbstractCoordinate);
        double distanceSecondToFirst = mockedSecondAbstractCoordinate.getCartesianDistance(mockedFirstAbstractCoordinate);

        // then
        assertEquals(distanceFirstToSecond, distanceSecondToFirst, 0.0);
        assertEquals(163.9862351113381, distanceFirstToSecond, 1e-12);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCartesianDistanceThrowsNullPointerException() {
        // given
        AbstractCoordinate mockedAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        // when
        mockedAbstractCoordinate.getCartesianDistance(null);
    }

    @Test
    public void testGetCentralAngle() {
        // given
        AbstractCoordinate mockedFirstAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        AbstractCoordinate mockedSecondAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        when(mockedFirstAbstractCoordinate.asSphericCoordinate())
                .thenReturn(new SphericCoordinate(Math.PI/4, Math.PI / 2, 42));
        when(mockedSecondAbstractCoordinate.asSphericCoordinate())
                .thenReturn(new SphericCoordinate(-3 * Math.PI/4, 0, 24));

        // when
        double centralAngleFirstToSecond = mockedFirstAbstractCoordinate.getCentralAngle(mockedSecondAbstractCoordinate);
        double centralAngleSecondToFirst = mockedSecondAbstractCoordinate.getCentralAngle(mockedFirstAbstractCoordinate);


        // then
        assertEquals(centralAngleFirstToSecond, centralAngleSecondToFirst, 0.0);
        assertEquals(Math.PI / 2, centralAngleFirstToSecond, 1e-12);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCentralAngleThrowsNullPointerException() {
        // given
        AbstractCoordinate mockedAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        // when
        mockedAbstractCoordinate.getCentralAngle(null);
    }

    @Test
    public void testExactEquality() {
        // given
        AbstractCoordinate mockedFirstAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        AbstractCoordinate mockedSecondAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        when(mockedFirstAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(7.654321, 8.7654321, 9.87654321));
        when(mockedSecondAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(7.654321, 8.7654321, 9.87654321));

        // then
        assertTrue(mockedFirstAbstractCoordinate.isEqual(mockedSecondAbstractCoordinate));
        assertTrue(mockedSecondAbstractCoordinate.isEqual(mockedFirstAbstractCoordinate));
    }

    @Test
    public void testJustEquality() {
        // given
        AbstractCoordinate mockedFirstAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        AbstractCoordinate mockedSecondAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        when(mockedFirstAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(7.6543, 8.7654, 9.8765));
        when(mockedSecondAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(7.6536, 8.7647, 9.8774));

        // then
        assertTrue(mockedFirstAbstractCoordinate.isEqual(mockedSecondAbstractCoordinate));
        assertTrue(mockedSecondAbstractCoordinate.isEqual(mockedFirstAbstractCoordinate));
    }

    @Test
    public void testInequality() {
        // given
        AbstractCoordinate mockedFirstAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        AbstractCoordinate mockedSecondAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        when(mockedFirstAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(9.87654321, 8.7654321, 7.654321));
        when(mockedSecondAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(7.654321, 8.7654321, 9.87654321));

        // then
        assertFalse(mockedFirstAbstractCoordinate.isEqual(mockedSecondAbstractCoordinate));
        assertFalse(mockedSecondAbstractCoordinate.isEqual(mockedFirstAbstractCoordinate));
    }

    @Test
    public void testAlmostEquality() {
        // given
        AbstractCoordinate mockedFirstAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);
        AbstractCoordinate mockedSecondAbstractCoordinate = mock(AbstractCoordinate.class, CALLS_REAL_METHODS);

        when(mockedFirstAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(7.6543, 8.7654, 9.8765));
        when(mockedSecondAbstractCoordinate.asCartesianCoordinate())
                .thenReturn(new CartesianCoordinate(7.6534, 8.7656, 9.8775));

        // then
        assertFalse(mockedFirstAbstractCoordinate.isEqual(mockedSecondAbstractCoordinate));
        assertFalse(mockedSecondAbstractCoordinate.isEqual(mockedFirstAbstractCoordinate));
    }
}
