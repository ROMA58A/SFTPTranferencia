package ols.doci.controllers;

import com.ols.doci.Log;
import com.ols.doci.db.Statements;
import com.sun.istack.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.StringJoiner;
import ols.doci.process.entity.Contactos;
import ols.doci.process.entity.DateFormatter;
import ols.doci.process.entity.Detalle;
import ols.doci.process.entity.DocumentMetadata;
import ols.doci.process.entity.Json;

/**
 * @author Brandon
 */
public class SerializerController
{
    @Nullable
    public static DocumentMetadata getMetadata(String itemnum)
    {
        String query = "SELECT * FROM WHERE " + itemnum;
        return Statements.executeSelect(query, (set) -> {
            try {
                return new DocumentMetadata(set);
            } catch (SQLException ex) {
                Log.error(ex, "On Creartxt#getMetadata");
                return null;
            }
        });
    }

    @Nullable
    public static String getJson(File jsonFile)
    {
        try {
            return Files.readString(jsonFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Log.error(ex, "On Creartxt#getJson");
            return null;
        }
    }

    public static String generateTxtContent(DocumentMetadata meta, Json json)
    {
        StringJoiner joiner = new StringJoiner(",");
        append(joiner, json.getTipoDocumento());
        append(joiner, meta.getCodigoGeneracion());
        append(joiner, meta.getSelloGeneracion());
        append(joiner, meta.getNumeroControl());
        append(joiner, DateFormatter. formatoFhProcesamiento(json.getFechaEmision()));
        append(joiner, json.getNombreCliente());
        append(joiner, json.get_num_doc_receptor());
        append(joiner, json.getNrcCliente());

        Detalle detalle = json.getDetalle().get(0);
        append(joiner, (detalle == null) ? " " : detalle.getDescripcion());

        append(joiner, String.valueOf(json.getSubTotalVentasGravadas()));
        append(joiner, String.valueOf(json.getSubTotalVentasExentas()));
        append(joiner, String.valueOf(json.getIva()));
        append(joiner, json.getDocRelNum());
        append(joiner, DateFormatter.FormatoFecha(meta.getFechaGeneracionMH()));

        Contactos contacto = json.getContactos().get(0);
        append(joiner, (contacto == null) ? " " : contacto.getEmail());

        return joiner.toString();
    }

    protected static void append(StringJoiner joiner, String value)
    {
        if (value == null || value.isEmpty()) {
            joiner.add("\" \"");
        } else {
            joiner.add("\"" + value + "\"");
        }
    }
}
