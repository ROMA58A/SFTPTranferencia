package com.ols.doci.db;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;
import static com.ols.doci.db.Database.SCHEMA;

/**
 * Utility class used to generate SQL query strings.
 *
 * @author Brandon
 */
public class Queries
{
    /**
     * Generates an SQL NEXT VALUE query string correctly.
     *
     * @param sequence on the database to use
     * @return the valid SQL query.
     */
    public static String SEQUENCE(String sequence)
    {
        return MessageFormat.format("SELECT NEXT VALUE FOR [{0}].[{1}]",
                SCHEMA, sequence);
    }

    /**
     * Generates an SQL INSERT query string correctly.
     *
     * @param table on the database to use
     * @param fields to be inserted
     * @param values to be inserted
     * @return the valid SQL query.
     */
    public static String INSERT(String table, String fields, Object... values)
    {
        return MessageFormat.format(
                "INSERT INTO [{0}].[{1}] ({2}) VALUES({3})",
                SCHEMA, table, fields,
                Values.prepare(values));
    }

    /**
     * Generates an SQL INSERT query string correctly.
     *
     * @param table on the database to use
     * @param entry to be inserted
     * @return the valid SQL query.
     */
    public static String INSERT(String table, Map<String, Object> entry)
    {
        Collection<String> fields = entry.keySet();
        return MessageFormat.format(
                "INSERT INTO [{0}].[{1}] ({2}) VALUES ({3})",
                SCHEMA, table,
                Values.prepareFields(fields),
                Values.prepare(fields, entry));
    }

    /**
     * Generates an SQL INSERT query string correctly.
     *
     * @param table on the database to use
     * @param fields to be inserted
     * @param entries to be inserted
     * @return the valid SQL query.
     */
    public static String INSERT(String table, Collection<String> fields, Collection<Map<String, Object>> entries)
    {
        StringJoiner values = new StringJoiner(",");
        // append the prepareValue rows
        for (Map<String, Object> entry : entries) {
            values.add("(" + Values.prepare(fields, entry) + ")");
        }
        // construct the full query
        return MessageFormat.format(
                "INSERT INTO [{0}].[{1}] ({2}) VALUES {3}",
                SCHEMA, table,
                Values.prepareFields(fields),
                values.toString());
    }

    /**
     * Generates an SQL INSERT query that can be used in a PreparedStatement
     *
     * @param table on the database to use
     * @param fields to be inserted
     * @return the valid SQL query.
     */
    public static String PINSERT(String table, Collection<String> fields)
    {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < fields.size(); i++) {
            joiner.add("?");
        }
        // construct the full query
        return MessageFormat.format(
                "INSERT INTO [{0}].[{1}] ({2}) VALUES ({3})",
                SCHEMA, table,
                Values.prepareFields(fields),
                joiner.toString());
    }

    /**
     * Generates an SQL UPDATE query string correctly.
     *
     * @param table on the database to use
     * @param where condition to reduce the affected entries
     * @param changes to apply on the entries
     * @return the valid SQL query.
     */
    public static String UPDATE(String table, String where, String changes)
    {
        return MessageFormat.format(
                "UPDATE [{0}].[{1}] SET {3} WHERE {2}",
                SCHEMA, table, where, changes);
    }

    /**
     * Generates an SQL SELECT query string correctly.
     *
     * @param table on the database to use
     * @param fields to be required
     * @return the valid SQL query.
     */
    public static String SELECT(String table, String fields)
    {
        return MessageFormat.format(
                "SELECT {2} FROM [{0}].[{1}]",
                SCHEMA, table, fields);
    }

    /**
     * Generates an SQL SELECT query string correctly.
     *
     * @param table on the database to use
     * @param fields to be required
     * @param where condition to reduce the returned entries
     * @return the valid SQL query.
     */
    public static String SELECT(String table, String fields, String where)
    {
        return MessageFormat.format(
                "SELECT {2} FROM [{0}].[{1}] WHERE {3}",
                SCHEMA, table, fields, where);
    }

    /**
     * Generates an SQL SELECT query string correctly.
     *
     * @param table on the database to use
     * @param fields to be required
     * @param where condition to reduce the returned entries
     * @param groupBy grouping conditions
     * @return the valid SQL query.
     */
    public static String SELECT(String table, String fields, String where, String groupBy)
    {
        return MessageFormat.format(
                "SELECT {2} FROM [{0}].[{1}] WHERE {3} GROUP BY {4}",
                SCHEMA, table, fields, where, groupBy);
    }

    /**
     * Generates an SQL SELECT query string correctly.
     *
     * @param table on the database to use
     * @param fields to be required
     * @param where condition to reduce the returned entries
     * @param groupBy grouping conditions
     * @param orderBy for row sorting
     * @return the valid SQL query.
     */
    public static String SELECT(String table, String fields, String where, String groupBy, String orderBy)
    {
        StringJoiner query = new StringJoiner(" ");
        query.add("SELECT " + fields + " FROM [" + SCHEMA + "].[" + table + "]");
        if (where != null && !where.isBlank()) {
            query.add("WHERE " + where);
        }
        if (groupBy != null && !groupBy.isBlank()) {
            query.add("GROUP BY " + groupBy);
        }
        if (orderBy != null && !orderBy.isBlank()) {
            query.add("ORDER BY " + orderBy);
        }
        return query.toString();
    }
}
