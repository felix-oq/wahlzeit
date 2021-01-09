package org.wahlzeit.model;

import org.junit.Test;
import org.wahlzeit.model.location.*;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;
import org.wahlzeit.utils.StringUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for the {@link Photo} class.
 */
public class PhotoTest {

    @Test
    public void testReadFrom() throws SQLException {
        // given
        int id = -1;
        int ownerId = 54321;
        String ownerName = "SomeOwner";
        boolean ownerNotifyAboutPraise = false;
        String ownerEmailAddress = "some@mail.com";
        int ownerLanguage = 3;
        String ownerHomePage = "http://www.some-homepage.com";
        int width = 256;
        int height = 128;
        String tags = "";
        int status = 1;
        int praiseSum = 12;
        int noVotes = 1;
        long creationTime = 123456789L;
        double coordinateX = 3.521;
        double coordinateY = 4712.42;
        double coordinateZ = 3.12e-8;

        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getInt("id")).thenReturn(id);
        when(mockedResultSet.getInt("owner_id")).thenReturn(ownerId);
        when(mockedResultSet.getString("owner_name")).thenReturn(ownerName);
        when(mockedResultSet.getBoolean("owner_notify_about_praise")).thenReturn(ownerNotifyAboutPraise);
        when(mockedResultSet.getString("owner_email_address")).thenReturn(ownerEmailAddress);
        when(mockedResultSet.getInt("owner_language")).thenReturn(ownerLanguage);
        when(mockedResultSet.getString("owner_home_page")).thenReturn(ownerHomePage);
        when(mockedResultSet.getInt("width")).thenReturn(width);
        when(mockedResultSet.getInt("height")).thenReturn(height);
        when(mockedResultSet.getString("tags")).thenReturn(tags);
        when(mockedResultSet.getInt("status")).thenReturn(status);
        when(mockedResultSet.getInt("praise_sum")).thenReturn(praiseSum);
        when(mockedResultSet.getInt("no_votes")).thenReturn(noVotes);
        when(mockedResultSet.getLong("creation_time")).thenReturn(creationTime);
        when(mockedResultSet.getInt("coordinate_type")).thenReturn(CoordinateType.Cartesian.ordinal());
        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(coordinateX);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(coordinateY);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(coordinateZ);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        Photo photo = new Photo();

        // when
        photo.readFrom(mockedResultSet);

        // then
        assertEquals(PhotoId.NULL_ID, photo.getId());
        assertEquals(ownerId, photo.getOwnerId());
        assertEquals(ownerName, photo.getOwnerName());
        assertEquals(ownerNotifyAboutPraise, photo.getOwnerNotifyAboutPraise());
        assertEquals(ownerEmailAddress, photo.getOwnerEmailAddress().asString());
        assertEquals(ownerLanguage, photo.getOwnerLanguage().ordinal());
        assertEquals(ownerHomePage, photo.getOwnerHomePage().toString());
        assertEquals(width, photo.getWidth());
        assertEquals(height, photo.getHeight());
        assertEquals(tags, photo.getTags().asString());
        assertEquals(status, photo.getStatus().asInt());
        assertEquals(praiseSum, photo.praiseSum);
        assertEquals(noVotes, photo.noVotes);
        assertEquals(creationTime, photo.getCreationTime());

        assertTrue(photo.location.coordinate instanceof CartesianCoordinate);
        CartesianCoordinate cartesianCoordinate = (CartesianCoordinate) photo.location.coordinate;

        assertEquals(coordinateX, cartesianCoordinate.getX(), 0.001);
        assertEquals(coordinateY, cartesianCoordinate.getY(), 0.001);
        assertEquals(coordinateZ, cartesianCoordinate.getZ(), 0.001);
    }

    @Test(expected = SQLException.class)
    public void testReadFromThrowsSQLExceptionWhenDatabaseFails() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getInt(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getLong(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getString(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getBoolean(anyString())).thenThrow(new SQLException());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new Photo(mockedResultSet);
    }

    @Test(expected = SQLException.class)
    public void testReadFromThrowsSQLExceptionWhenDatabaseHasInvalidSchema() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new Photo(mockedResultSet);
    }

    @Test(expected = SQLException.class)
    public void testReadFromThrowsSQLExceptionWhenDatabaseContainsUninterpretableCoordinateType() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getInt("coordinate_type")).thenReturn(-1);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        new Photo(mockedResultSet);
    }

    @Test
    public void testWriteOn() throws SQLException {
        // given
        PhotoId id = PhotoId.NULL_ID;
        int ownerId = 42;
        String ownerName = "Max Mustermann";
        boolean ownerNotifyAboutPraise = true;
        String ownerEmailAddress = "max@fakemail.org";
        int ownerLanguage = 1;
        String ownerHomePage = "http://www.maxmustermann.de";
        int width = 256;
        int height = 512;
        String tags = "a, b, c";
        int status = 0;
        int praiseSum = 2;
        int noVotes = 0;
        long creationTime = 999L;
        double coordinateX = 0.24;
        double coordinateY = 10423.43;
        double coordinateZ = -1.2;

        Photo photo = new Photo();
        photo.id = id;
        photo.ownerId = ownerId;
        photo.ownerName = ownerName;
        photo.ownerNotifyAboutPraise = ownerNotifyAboutPraise;
        photo.ownerEmailAddress = EmailAddress.getFromString(ownerEmailAddress);
        photo.ownerLanguage = Language.getFromInt(ownerLanguage);
        photo.ownerHomePage = StringUtil.asUrl(ownerHomePage);
        photo.width = width;
        photo.height = height;
        photo.tags = new Tags(tags);
        photo.status = PhotoStatus.getFromInt(status);
        photo.praiseSum = praiseSum;
        photo.noVotes = noVotes;
        photo.creationTime = creationTime;
        CartesianCoordinate coordinate = CartesianCoordinate.getValueObject(coordinateX, coordinateY, coordinateZ);
        photo.location = new Location(coordinate);

        ResultSet mockedResultSet = mock(ResultSet.class);

        when(mockedResultSet.getDouble("coordinate_1")).thenReturn(coordinateX);
        when(mockedResultSet.getDouble("coordinate_2")).thenReturn(coordinateY);
        when(mockedResultSet.getDouble("coordinate_3")).thenReturn(coordinateZ);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        // when
        photo.writeOn(mockedResultSet);

        // then
        verify(mockedResultSet, times(1)).updateInt("id", id.asInt());
        verify(mockedResultSet, times(1)).updateInt("owner_id", ownerId);
        verify(mockedResultSet, times(1)).updateString("owner_name", ownerName);
        verify(mockedResultSet, times(1)).updateBoolean("owner_notify_about_praise", ownerNotifyAboutPraise);
        verify(mockedResultSet, times(1)).updateString("owner_email_address", ownerEmailAddress);
        verify(mockedResultSet, times(1)).updateInt("owner_language", ownerLanguage);
        verify(mockedResultSet, times(1)).updateString("owner_home_page", ownerHomePage);
        verify(mockedResultSet, times(1)).updateInt("width", width);
        verify(mockedResultSet, times(1)).updateInt("height", height);
        verify(mockedResultSet, times(1)).updateString("tags", tags);
        verify(mockedResultSet, times(1)).updateInt("status", status);
        verify(mockedResultSet, times(1)).updateInt("praise_sum", praiseSum);
        verify(mockedResultSet, times(1)).updateInt("no_votes", noVotes);
        verify(mockedResultSet, times(1)).updateLong("creation_time", creationTime);
        verify(mockedResultSet, times(1)).updateInt("coordinate_type", CoordinateType.Cartesian.ordinal());
        verify(mockedResultSet, times(1)).updateDouble("coordinate_1", coordinateX);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_2", coordinateY);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_3", coordinateZ);
        verify(mockedResultSet, atLeastOnce()).getMetaData();
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLExceptionWhenDatabaseFails() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        doThrow(new SQLException()).when(mockedResultSet).updateDouble(anyString(), anyDouble());
        doThrow(new SQLException()).when(mockedResultSet).updateInt(anyString(), anyInt());
        doThrow(new SQLException()).when(mockedResultSet).updateLong(anyString(), anyLong());
        doThrow(new SQLException()).when(mockedResultSet).updateString(anyString(), anyString());
        doThrow(new SQLException()).when(mockedResultSet).updateBoolean(anyString(), anyBoolean());

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createValidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        Photo photo = new Photo();

        // when
        photo.writeOn(mockedResultSet);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLExceptionWhenDatabaseHasInvalidSchema() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);

        ResultSetMetaData mockedMetaData = ResultSetMockingUtils.createInvalidResultSetMetaDataMock();
        when(mockedResultSet.getMetaData()).thenReturn(mockedMetaData);

        Photo photo = new Photo();

        // when
        photo.writeOn(mockedResultSet);
    }

}
