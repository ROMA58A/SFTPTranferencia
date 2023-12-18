package ols.doci.process.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author Brandon
 */
public class DocumentMetadata {

    public DocumentMetadata(ResultSet set) throws SQLException {
        this.numeroControl = set.getString("numcontrol");
        this.codigoGeneracion = set.getString("codigogeneracionhda");
        this.fechaGeneracionMH = set.getTimestamp("fechahprocesamiento");
        this.selloGeneracion = set.getString("sellohda");
       
        
    }

    private final String codigoGeneracion;
    private final String selloGeneracion;
    private final String numeroControl;
    private final Timestamp fechaGeneracionMH;
    
    
   

    // getters
    public String getCodigoGeneracion() {
        return codigoGeneracion;
    }

    public String getSelloGeneracion() {
        return selloGeneracion;
    }

    public String getNumeroControl() {
        return numeroControl;
    }

    public Timestamp getFechaGeneracionMH() {
        return fechaGeneracionMH;
    }

  
}
