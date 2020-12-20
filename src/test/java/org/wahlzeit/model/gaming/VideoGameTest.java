package org.wahlzeit.model.gaming;

import org.junit.Test;
import org.wahlzeit.model.location.ResultSetMockingUtils;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

/**
 * Test cases for the {@link VideoGame} class.
 */
public class VideoGameTest {

    @Test(expected = NullPointerException.class)
    public void testAttributeConstructorThrowsNullPointerException() {
        // when
        new VideoGame(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAttributeConstructorThrowsIllegalArgumentException() {
        // when
        new VideoGame("  \t ", VideoGameGenre.Unknown, new Date(0L));
    }

    @Test(expected = SQLException.class)
    public void testResultSetConstructorThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getString(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getInt(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getDate(anyString())).thenThrow(new SQLException());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new VideoGame(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResultSetConstructorThrowsIllegalArgumentExceptionWhenDatabaseHasInvalidSchema() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new VideoGame(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResultSetConstructorThrowsIllegalArgumentExceptionWhenTitleIsBlank() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getString(anyString())).thenReturn("  \t ");
        when(mockedResultSet.getInt(anyString())).thenReturn(0);
        when(mockedResultSet.getDate(anyString())).thenReturn(new Date(0L));

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new VideoGame(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testResultSetConstructorThrowsNullPointerException() throws SQLException {
        // when
        new VideoGame(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testResultSetConstructorThrowsIndexOutOfBoundsException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getString(anyString())).thenReturn("dummy");
        when(mockedResultSet.getInt(anyString())).thenReturn(VideoGameGenre.values().length);
        when(mockedResultSet.getDate(anyString())).thenReturn(new Date(0L));

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new VideoGame(mockedResultSet);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        /// given
        ResultSet mockedResultSet = mock(ResultSet.class);
        doThrow(new SQLException()).when(mockedResultSet).updateString(anyString(), anyString());
        doThrow(new SQLException()).when(mockedResultSet).updateInt(anyString(), anyInt());
        doThrow(new SQLException()).when(mockedResultSet).updateDate(anyString(), any());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        VideoGame videoGame = new VideoGame("dummy", VideoGameGenre.Unknown, new Date(0));

        // when
        videoGame.writeOn(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteOnThrowsIllegalArgumentException() throws SQLException {
        /// given
        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        VideoGame videoGame = new VideoGame("dummy", VideoGameGenre.Unknown, new Date(0));

        // when
        videoGame.writeOn(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        VideoGame videoGame = new VideoGame("dummy", VideoGameGenre.Unknown, new Date(0));

        // when
        videoGame.writeOn(null);
    }
}
