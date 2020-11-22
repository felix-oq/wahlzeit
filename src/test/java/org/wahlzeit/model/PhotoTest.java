package org.wahlzeit.model;

import org.junit.Test;
import org.wahlzeit.services.EmailAddress;
import org.wahlzeit.services.Language;
import org.wahlzeit.utils.StringUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
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

        ResultSet resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getInt("id")).thenReturn(id);
        when(resultSetMock.getInt("owner_id")).thenReturn(ownerId);
        when(resultSetMock.getString("owner_name")).thenReturn(ownerName);
        when(resultSetMock.getBoolean("owner_notify_about_praise")).thenReturn(ownerNotifyAboutPraise);
        when(resultSetMock.getString("owner_email_address")).thenReturn(ownerEmailAddress);
        when(resultSetMock.getInt("owner_language")).thenReturn(ownerLanguage);
        when(resultSetMock.getString("owner_home_page")).thenReturn(ownerHomePage);
        when(resultSetMock.getInt("width")).thenReturn(width);
        when(resultSetMock.getInt("height")).thenReturn(height);
        when(resultSetMock.getString("tags")).thenReturn(tags);
        when(resultSetMock.getInt("status")).thenReturn(status);
        when(resultSetMock.getInt("praise_sum")).thenReturn(praiseSum);
        when(resultSetMock.getInt("no_votes")).thenReturn(noVotes);
        when(resultSetMock.getLong("creation_time")).thenReturn(creationTime);
        when(resultSetMock.getDouble("coordinate_x")).thenReturn(coordinateX);
        when(resultSetMock.getDouble("coordinate_y")).thenReturn(coordinateY);
        when(resultSetMock.getDouble("coordinate_z")).thenReturn(coordinateZ);

        Photo photo = new Photo();

        // when
        photo.readFrom(resultSetMock);

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
        assertEquals(coordinateX, photo.location.coordinate.getX(), 0.0);
        assertEquals(coordinateY, photo.location.coordinate.getY(), 0.0);
        assertEquals(coordinateZ, photo.location.coordinate.getZ(), 0.0);
    }

    @Test(expected = SQLException.class)
    public void testReadFromThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        when(mockedResultSet.getDouble(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getInt(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getLong(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getString(anyString())).thenThrow(new SQLException());
        when(mockedResultSet.getBoolean(anyString())).thenThrow(new SQLException());

        // when
        new Location(mockedResultSet);
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
        double coordinateX = 0.0024;
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
        Coordinate coordinate = new Coordinate(coordinateX, coordinateY, coordinateZ);
        photo.location = new Location(coordinate);

        ResultSet mockedResultSet = mock(ResultSet.class);

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
        verify(mockedResultSet, times(1)).updateDouble("coordinate_x", coordinateX);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_y", coordinateY);
        verify(mockedResultSet, times(1)).updateDouble("coordinate_z", coordinateZ);
        verifyNoMoreInteractions(mockedResultSet);
    }

    @Test(expected = SQLException.class)
    public void testWriteOnThrowsSQLException() throws SQLException {
        // given
        ResultSet mockedResultSet = mock(ResultSet.class);
        doThrow(new SQLException()).when(mockedResultSet).updateDouble(anyString(), anyDouble());
        doThrow(new SQLException()).when(mockedResultSet).updateInt(anyString(), anyInt());
        doThrow(new SQLException()).when(mockedResultSet).updateLong(anyString(), anyLong());
        doThrow(new SQLException()).when(mockedResultSet).updateString(anyString(), anyString());
        doThrow(new SQLException()).when(mockedResultSet).updateBoolean(anyString(), anyBoolean());

        Photo photo = new Photo();

        // when
        photo.writeOn(mockedResultSet);
    }

}