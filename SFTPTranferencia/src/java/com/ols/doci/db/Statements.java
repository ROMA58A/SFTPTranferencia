package com.ols.doci.db;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import com.ols.doci.Log;
import static com.ols.doci.db.Database.DEBUG;

/**
 * Utility class used to perform SQL statements execution.
 *
 * @author Luis Brayan
 */
public class Statements
{
    /**
     * Constructs a valid statement.
     *
     * @return
     * @throws SQLException
     */
    public static Statement create() throws SQLException
    {
        return Database.getConnection().createStatement();
    }

    /**
     * Performs a simple SQL SEQUENCE query execution.
     *
     * @param sequence
     * @return
     */
    public static int getUniqueId(String sequence)
    {
        String query = Queries.SEQUENCE(sequence);
        if (DEBUG) {
            Log.debug("SEQUENCE query: <{0}>", query);
        }
        try (Statement statement = create(); ResultSet set = statement.executeQuery(query)) {
            if (set.next()) {
                return set.getInt(1);
            }
        } catch (SQLException ex) {
            Log.error(ex, "Problems during query execution <{0}>", query);
        }
        return 0;
    }

    /**
     * Performs a simple SQL SELECT query execution.
     *
     * @param query to be executed
     * @param handler called on each row of the select
     * @return whether the execution was successful.
     */
    public static boolean executeSelect(String query, Consumer<ResultSet> handler)
    {
        if (DEBUG) {
            Log.debug("Executing query <{0}>", query);
        }
        try (Statement statement = create(); ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                handler.accept(set);
            }
        } catch (Exception ex) {
            Log.error(ex, "Problems during query execution <{0}>", query);
            return false;
        }
        return true;
    }

    /**
     * Performs a simple SQL SELECT query execution.
     *
     * @param <R>
     * @param query to be executed
     * @param handler called on each row of the select
     * @return whether the execution was successful.
     */
    @Nullable
    public static <R> R executeSelect(String query, Function<ResultSet, R> handler)
    {
        if (DEBUG) {
            Log.debug("Executing query <{0}>", query);
        }
        try (Statement statement = create(); ResultSet set = statement.executeQuery(query)) {
            while (set.next()) {
                return handler.apply(set);
            }
        } catch (Exception ex) {
            Log.error(ex, "Problems during query execution <{0}>", query);
        }
        return null;
    }

    /**
     * Performs a simple SQL query execution.
     *
     * @param query to be executed
     * @return whether the execution was successful.
     */
    public static int executeUpdate(String query)
    {
        if (DEBUG) {
            Log.debug("Executing query <{0}>", query);
        }
        try (Statement statement = create()) {
            return statement.executeUpdate(query);
        } catch (SQLException ex) {
            Log.error(ex, "Problems during query execution <{0}>", query);
            return -1;
        }
    }

    /**
     * Performs a Prepared SQL query execution.
     *
     * @param table to be affected
     * @param values to be inserted
     * @return whether the execution was successful.
     */
    public static int executeInsert(String table, Map<String, Object> values)
    {
        return executeInsert(table, values.keySet(), values);
    }

    /**
     * Performs a Prepared SQL query execution.
     *
     * @param table to be affected
     * @param order of the values
     * @param values to be inserted
     * @return whether the execution was successful.
     */
    public static int executeInsert(String table, Collection<String> order, Map<String, Object> values)
    {
        String query = Queries.PINSERT(table, order);
        if (DEBUG) {
            Log.debug("Prepared query: <{0}>", query);
        }
        try (PreparedStatement statement = Database.getConnection().prepareStatement(query)) {
            Values.fill(statement, order, values);
            return statement.executeUpdate();
        } catch (SQLException ex) {
            Log.error("Problematic query serialized as <{0}>", Queries.INSERT(table, values));
            Log.error(ex, "Problems during query execution <{0}>", query);
            return -1;
        }
    }

    /**
     * Performs a Prepared SQL query execution.
     *
     * @param table to be affected
     * @param order of the values
     * @param values to be inserted
     * @return whether the execution was successful.
     */
    public static int executeInsert(String table, Collection<String> order, Collection<Object> values)
    {
        if (order.size() != values.size()) {
            Log.error("On Statements#executeInsert: Fields and values lists should be of the same length.");
            return -1;
        }
        
        String query = Queries.PINSERT(table, order);
        if (DEBUG) {
            Log.debug("Prepared query: <{0}>", query);
        }
        try (PreparedStatement statement = Database.getConnection().prepareStatement(query)) {
            Values.fill(statement, values);
            return statement.executeUpdate();
        } catch (Exception ex) {
            Log.error("Problematic query serialized as <{0}>", Queries.INSERT(table, Values.prepareFields(order), values));
            Log.error(ex, "Problems during query execution <{0}>", query);
            return -1;
        }
    }

    /**
     * Performs a Prepared SQL query execution.
     *
     * @param table to be affected
     * @param order of the values
     * @param values to be inserted
     * @param idField on the table to return
     * @return id of the inserted line, or {@code -1} if any problem happens.
     */
    public static int executeInsert(String table, Collection<String> order, Collection<Object> values, @NotNull String idField)
    {
        if (order.size() != values.size()) {
            Log.error("On Statements#executeInsert: Fields and values lists should be of the same length.");
            return -1;
        }
        
        String[] keys = new String[]{idField};
        String query = Queries.PINSERT(table, order);
        if (DEBUG) {
            Log.debug("Prepared query: <{0}>", query);
        }
        try (PreparedStatement statement = Database.getConnection().prepareStatement(query, keys)) {
            Values.fill(statement, values);
            if (statement.executeUpdate() < 1) {
                return -1;
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (Exception ex) {
            Log.error("Problematic query serialized as <{0}>", Queries.INSERT(table, Values.prepareFields(order), values));
            Log.error(ex, "Problems during query execution <{0}>", query);
        }
        return -1;
    }
}
