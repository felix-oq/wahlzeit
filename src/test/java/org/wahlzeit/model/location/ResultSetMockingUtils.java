package org.wahlzeit.model.location;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class provides some convenience methods that help creating proper result set mocks.
 */
public class ResultSetMockingUtils {

    public static ResultSetMetaData createValidResultSetMetaDataMock() throws SQLException {
        ResultSetMetaData mockedMetaData = mock(ResultSetMetaData.class);

        when(mockedMetaData.getColumnCount()).thenReturn(6);

        when(mockedMetaData.getColumnLabel(1)).thenReturn("additional");
        when(mockedMetaData.getColumnType(1)).thenReturn(Types.VARCHAR);

        when(mockedMetaData.getColumnLabel(2)).thenReturn("coordinate_type");
        when(mockedMetaData.getColumnType(2)).thenReturn(Types.INTEGER);

        when(mockedMetaData.getColumnLabel(3)).thenReturn("unnecessary");
        when(mockedMetaData.getColumnType(3)).thenReturn(Types.DATE);

        when(mockedMetaData.getColumnLabel(4)).thenReturn("coordinate_1");
        when(mockedMetaData.getColumnType(4)).thenReturn(Types.DOUBLE);

        when(mockedMetaData.getColumnLabel(5)).thenReturn("coordinate_2");
        when(mockedMetaData.getColumnType(5)).thenReturn(Types.DOUBLE);

        when(mockedMetaData.getColumnLabel(6)).thenReturn("coordinate_3");
        when(mockedMetaData.getColumnType(6)).thenReturn(Types.DOUBLE);

        return mockedMetaData;
    }

    public static ResultSetMetaData createInvalidResultSetMetaDataMock() throws SQLException {
        ResultSetMetaData mockedMetaData = mock(ResultSetMetaData.class);

        when(mockedMetaData.getColumnCount()).thenReturn(4);

        when(mockedMetaData.getColumnLabel(1)).thenReturn("additional");
        when(mockedMetaData.getColumnType(1)).thenReturn(Types.VARCHAR);

        when(mockedMetaData.getColumnLabel(2)).thenReturn("coordinate_type");
        when(mockedMetaData.getColumnType(2)).thenReturn(Types.BIT);

        when(mockedMetaData.getColumnLabel(3)).thenReturn("coordinate_1");
        when(mockedMetaData.getColumnType(3)).thenReturn(Types.DOUBLE);

        when(mockedMetaData.getColumnLabel(4)).thenReturn("coordinate_3");
        when(mockedMetaData.getColumnType(4)).thenReturn(Types.DOUBLE);

        return mockedMetaData;
    }

}
