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
        new VideoGame("  \t ", new VideoGameType(""), new Date(0L));
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

        VideoGame videoGame = new VideoGame("dummy", new VideoGameType(""), new Date(0));

        // when
        videoGame.writeOn(mockedResultSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteOnThrowsIllegalArgumentException() throws SQLException {
        /// given
        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        VideoGame videoGame = new VideoGame("dummy", new VideoGameType(""), new Date(0));

        // when
        videoGame.writeOn(mockedResultSet);
    }

    @Test(expected = NullPointerException.class)
    public void testWriteOnThrowsNullPointerException() throws SQLException {
        VideoGame videoGame = new VideoGame("dummy", new VideoGameType(""), new Date(0));

        // when
        videoGame.writeOn(null);
    }
}
