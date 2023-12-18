package ols.doci.sftp.db;

import com.ols.doci.Log;
import com.ols.doci.db.Queries;
import com.ols.doci.db.Statements;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Brandon 
 */
public class Entries {

    public static int TABLE_LIMIT = 100;

    public static List<Long> getAceptedBatch() {
        return getBatch("");
    }

    public static List<Long> getRejectedBatch() {
        return getBatch("");
    }

    protected static List<Long> getBatch(String conditions) {
       

        String query = "SELECT DISTINCT TOP(" + TABLE_LIMIT + ") "
                + " FROM "
           
                + " WHERE " + conditions
                + " ORDER BY T.itemnum DESC";
        
        List<Long> batch = new ArrayList<>();
        Statements.executeSelect(query, (set) -> {
            // the catch is inside
            // to avoid not-processing good entries
            // for errores on bad entries
            try {
                batch.add(set.getLong("itemnum"));
            } catch (SQLException ex) {
                Log.error(ex, "On Entries#getBatch");
            }
        });
        return batch;
    }

    public static boolean updateEntry(String itemnum, int newStatus) {
        String query = Queries.UPDATE("itemsdata",
                /* where */ "itemnum=" + itemnum,
                /* set   */ "statusxport=" + newStatus);
        return Statements.executeUpdate(query) > 0;
    }
}
