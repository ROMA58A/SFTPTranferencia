package com.ols.doci.db;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Utility class to prepare SQL query values.
 *
 * @author Brandon
 */
public class Values
{
    public static final DateTimeFormatter DTF;

    static {
        DTF = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral('T')
                .appendValue(ChronoField.HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                .appendLiteral(':')
                .appendValue(ChronoField.SECOND_OF_MINUTE, 2)
                .toFormatter();
    }

    /**
     * Generates a string usable on SQL queries of a list of table fields.
     *
     * @param fields the fields to serialize.
     * @return the valid SQL query.
     */
    public static String prepareFields(Collection<String> fields)
    {
        StringJoiner result = new StringJoiner(", ");
        for (String field : fields) {
            result.add("[" + field + "]");
        }
        return result.toString();
    }

    /**
     * Prepares a prepareValue for an SQL query, respecting it types.
     *
     * @param value to serialize
     * @return the SQL compatible value string.
     */
    public static String prepareValue(Object value)
    {
        // reserved word
        if (value == null) {
            return "NULL";
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof TemporalAccessor) {
            return "'" + DTF.format((TemporalAccessor) value) + "'";
        }
        // wrap with ' for standard string input
        return "'" + value.toString() + "'";
    }

    /**
     * Generates a string usable on SQL queries, respecting prepareValue types.
     *
     * @param <T>
     * @param values the prepare to serialize.
     * @return the valid SQL query.
     */
    public static <T> String prepare(T[] values)
    {
        StringJoiner result = new StringJoiner(",");
        for (Object value : values) {
            result.add(prepareValue(value));
        }
        return result.toString();
    }

    /**
     * Generates a string usable on SQL queries, respecting prepareValue types.
     *
     * @param <T>
     * @param values the prepare to serialize.
     * @return the valid SQL query.
     */
    public static <T> String prepare(Collection<T> values)
    {
        StringJoiner result = new StringJoiner(",");
        for (Object value : values) {
            result.add(prepareValue(value));
        }
        return result.toString();
    }

    /**
     * Generates a string usable on SQL queries, respecting prepareValue types.
     *
     * @param order the sequence to place the prepare.
     * @param values the prepare to serialize.
     * @return the valid SQL query.
     */
    public static String prepare(Collection<String> order, Map<String, Object> values)
    {
        StringJoiner result = new StringJoiner(",");
        for (String field : order) {
            result.add(prepareValue(values.get(field)));
        }
        return result.toString();
    }

    /**
     * Fills a single value on a PreparedStatement.
     *
     * @param statement to fill
     * @param parameterIndex to set
     * @param value to set
     * @throws SQLException
     */
    public static void fillValue(PreparedStatement statement, int parameterIndex, Object value) throws SQLException
    {
        if (value == null) {
            statement.setNull(parameterIndex, java.sql.Types.NULL);
        } else if (value instanceof String) {
            statement.setString(parameterIndex, (String) value);
        } else if (value instanceof Boolean) {
            statement.setBoolean(parameterIndex, (Boolean) value);
        } //
        //
        // Numbers
        else if (value instanceof Integer) {
            statement.setInt(parameterIndex, (Integer) value);
        } else if (value instanceof Long) {
            statement.setLong(parameterIndex, (Long) value);
        } else if (value instanceof Float) {
            statement.setFloat(parameterIndex, (Float) value);
        } else if (value instanceof Double) {
            statement.setDouble(parameterIndex, (Double) value);
        } else if (value instanceof BigDecimal) {
            statement.setBigDecimal(parameterIndex, (BigDecimal) value);
        } //
        //
        // Date
        else if (value instanceof Date) {
            statement.setDate(parameterIndex, (Date) value);
        } else if (value instanceof LocalDate) {
            statement.setDate(parameterIndex, Date.valueOf((LocalDate) value));
        }//
        //
        // Date & Time
        else if (value instanceof Timestamp) {
            statement.setTimestamp(parameterIndex, (Timestamp) value);
        } else if (value instanceof LocalDateTime) {
            statement.setTimestamp(parameterIndex, Timestamp.valueOf((LocalDateTime) value));
        } else {
            statement.setString(parameterIndex, prepareValue(value));
        }
    }

    /**
     * Fills a group of fills in the order of the values.
     *
     * @param statement to fill
     * @param values to set
     * @throws SQLException
     */
    public static void fill(PreparedStatement statement, Iterable<Object> values) throws SQLException
    {
        int i = 1;
        for (Object value : values) {
            fillValue(statement, i, value);
            i++;
        }
    }

    /**
     * Fills a PreparedStatement respecting the prepareValue types.
     *
     * @param statement to fill
     * @param order to set the values
     * @param values to set
     * @throws SQLException
     */
    public static void fill(PreparedStatement statement, Iterable<String> order, Map<String, Object> values) throws SQLException
    {
        int i = 1;
        for (String name : order) {
            fillValue(statement, i, values.get(name));
            i++;
        }
    }
}
