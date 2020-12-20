package org.wahlzeit.utils;

import java.sql.*;

/**
 * Provides convenience methods that are commonly needed for assertions.
 */
public class Assertions {

    /**
     * Throws an {@link IllegalArgumentException} if the condition is not met.
     * @param condition the condition that might be met
     * @param message the error message for the exception if the condition is not met
     * @throws NullPointerException if the condition is not met
     */
    public static void checkArgument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Throws a {@link NullPointerException} if the entered object is null.
     * @param object the object to check
     * @param message the error message for the exception if the object is null
     * @throws NullPointerException if the object is null
     */
    public static void checkNotNull(Object object, String message) {
        if (object == null)
            throw new NullPointerException(message);
    }

    /**
     * Checks if the entered string is not blank. A string is considered as blank if it is empty or if it only contains
     * whitespaces.
     * @param string the string to check
     * @param message the error message for the exception if the string is blank
     * @throws IllegalArgumentException if the string is blank
     */
    public static void checkStringArgumentIsNotBlank(String string, String message) {
            if (string.trim().isEmpty())
                throw new IllegalArgumentException(message);
    }

    /**
     * Throws a {@link IndexOutOfBoundsException} if the entered index is not within the given range.
     * @param index the index to check
     * @param minIndexIncluded the begin of the range (included)
     * @param maxIndexExcluded the end of the range (excluded)
     * @param message the error message for the exception if the index is out of the range
     * @throws IndexOutOfBoundsException if the index is out of the range
     */
    public static void checkInRange(int index, int minIndexIncluded, int maxIndexExcluded, String message) {
        if (index < minIndexIncluded || maxIndexExcluded <= index)
            throw new IndexOutOfBoundsException(message);
    }

    /**
     * Throws a {@link IndexOutOfBoundsException} if the entered index is not within the given range. The range starts
     * at zero which is included in the range.
     * @param index the index to check
     * @param maxIndexExcluded the end of the range (excluded)
     * @param message the error message for the exception if the index is out of the range
     * @throws IndexOutOfBoundsException if the index is out of the range
     */
    public static void checkInRange(int index, int maxIndexExcluded, String message) {
        checkInRange(index, 0, maxIndexExcluded, message);
    }

    /**
     * Checks if the entered double is finite.
     * @param d the double to check
     * @param message the error message for the exception if the double is not finite
     * @throws IllegalArgumentException if the double is not finite
     */
    public static void checkDoubleArgumentIsFinite(double d, String message) {
        checkArgument(Double.isFinite(d), message);
    }

    /**
     * Checks if the entered result set has a column with a certain label and a certain type.
     * @param rset the result set to check
     * @param columnLabel the label that a column in the result set should have
     * @param sqlType the sql type that the column should have (choose a value of {@link Types})
     * @throws IllegalArgumentException if the result set does not have a column with the specified label or if the
     *                                  column with the specified label does not have the specified type
     * @throws SQLException if the database cannot be accessed or if the result set is closed
     */
    public static void checkResultSetArgumentHasColumnAndType(ResultSet rset, String columnLabel, int sqlType)
        throws SQLException {

        ResultSetMetaData metaData = rset.getMetaData();

        int columnIndex = checkResultSetArgumentHasColumn(metaData, columnLabel,
                "The column '" + columnLabel + "' must exist");
        checkResultSetArgumentHasColumnType(metaData, columnIndex, sqlType,
                "The column '" + columnLabel + "' must be of type " + JDBCType.valueOf(sqlType).getName());
    }

    private static int checkResultSetArgumentHasColumn(ResultSetMetaData metaData, String columnLabel, String message) throws SQLException {
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); ++columnIndex) {
            if (metaData.getColumnLabel(columnIndex).equals(columnLabel))
                return columnIndex;
        }
        throw new IllegalArgumentException(message);
    }

    private static void checkResultSetArgumentHasColumnType(ResultSetMetaData metaData, int columnIndex, int sqlType, String message) throws SQLException {
        checkArgument(metaData.getColumnType(columnIndex) == sqlType, message);
    }

}

