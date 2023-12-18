package ols.doci.process;

import com.google.gson.Gson;
import com.ols.doci.EnvLoader;
import com.ols.doci.FileSystem;
import com.ols.doci.Log;
import com.ols.doci.db.Statements;
import com.ols.doci.db.Values;
import com.ols.doci.job.CronJob;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import ols.doci.controllers.SerializerController;
import static ols.doci.controllers.SerializerController.generateTxtContent;
import ols.doci.process.entity.DocumentMetadata;
import ols.doci.process.entity.Json;
import ols.doci.process.pdf.DociApiConsumer;
import ols.doci.process.pdf.DociPdfPayload;
import ols.doci.process.pdf.DociStoragePayload;
import ols.doci.process.pdf.DociStorageResponse;
import ols.doci.sftp.db.Entries;
import org.quartz.JobExecutionContext;

/**
 * @author Brandon
 */
public class SerializerJob extends CronJob {

    public static void init() {
        SHOULD_RUN = EnvLoader.getBoolean("jobs", "serializerShouldRun");

        COMPANY_ID = EnvLoader.getString("companyId", "id");
        NIT_BANCO = EnvLoader.getString("Nit", "banco");
        NIT_TARJETAS = EnvLoader.getString("Nit", "tarjetas");
        LOCAL_FOLDER_ACEPTED = EnvLoader.getString("filesystem", "localAceptedFolder");
        LOCAL_FOLDER_REJECTED = EnvLoader.getString("filesystem", "localRejectedFolder");

        LOCAL_FOLDER_ACEPTEDBac = EnvLoader.getString("filesystem", "localAceptedFolderBac");
         LOCAL_FOLDER_ACEPTEDBac = EnvLoader.getString("filesystem", "localRejectedFolderBac");

    }

    protected static boolean SHOULD_RUN;

    private static String COMPANY_ID;
    private static String NIT_BANCO;
    private static String NIT_TARJETAS;
    private static String LOCAL_FOLDER_ACEPTED;
    private static String LOCAL_FOLDER_REJECTED;
    private static String LOCAL_FOLDER_ACEPTEDBac;
    private static String LOCAL_FOLDER_REJECTEDBac;

    @Override
    protected boolean hasWorkToDo(JobExecutionContext context) {
        if (!SHOULD_RUN) {
            return false;
        }

        // define the batch to process
        List<Long> aceptedItemnums = Entries.getAceptedBatch();
        List<Long> rejectedItemnums = Entries.getRejectedBatch();
        if (aceptedItemnums.isEmpty() && rejectedItemnums.isEmpty()) {
            return false;
        }

        // prepare current execution
        aceptedBatchSize = aceptedItemnums.size();
        aceptedBatch = Values.prepare(aceptedItemnums);

        rejectedBatchSize = rejectedItemnums.size();
        rejectedBatch = Values.prepare(rejectedItemnums);

        return true;
    }

    private int aceptedBatchSize;
    private String aceptedBatch;

    private int rejectedBatchSize;
    private String rejectedBatch;

    @Override
    protected void main(JobExecutionContext context) throws Exception {
        GSON = new Gson();

        processRejectedBatch();
        processAceptedBatch();
    }

    public Gson GSON;

    protected void processRejectedBatch() {
        if (rejectedBatchSize < 1) {
            return;
        }

        startProcessing(rejectedBatch, (doc) -> {
            Log.log("***** procesando rejected itemnum <{0}>", doc.itemnum);

            // Nombre de la carpeta basado en el código de generación
            String folderName = doc.meta.getCodigoGeneracion();
            File folder = new File(determineFolderByNit(doc, false), folderName);

            // Asegurar la extensión .txt para el archivo de texto
            File txtFile = new File(folder, folderName + ".txt");

            try {
                // Crear la carpeta si no existe
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                FileSystem.writeFile(txtFile, generateTxtContent(doc.meta, doc.json), StandardCharsets.UTF_8);
                Entries.updateEntry(doc.itemnum, 4);
                Log.log("Itemnum [{0}] estatus cambiado a [4]PROCESSED", doc.itemnum);
            } catch (IOException ex) {
                Entries.updateEntry(doc.itemnum, 5);
                Log.error("Itemnum [{0}] estatus cambiado a [5]PROBLEMATIC", doc.itemnum);
            }
        });
    }

    private String determineFolderByNit(DocumentData doc, boolean isAcceptedBatch) {
        String nit = doc.json != null ? doc.json.getNit() : null;  // Obtener el NIT del objeto Json

        if (NIT_BANCO.equals(nit)) {
            return isAcceptedBatch ? LOCAL_FOLDER_ACEPTEDBac : LOCAL_FOLDER_REJECTEDBac;
        } else if (NIT_TARJETAS.equals(nit)) {
            return isAcceptedBatch ? LOCAL_FOLDER_ACEPTED : LOCAL_FOLDER_REJECTED;
        } else {
            return null;  // Retornar null si el NIT no corresponde a ninguno de los conocidos
        }
    }

    protected void processAceptedBatch() {
        if (aceptedBatchSize < 1) {
            return;
        }

        startProcessing(aceptedBatch, (doc) -> {
            Log.log("***** procesando accepted itemnum <{0}>", doc.itemnum);

            // Nombre de la carpeta basado en el código de generación
            String folderName = doc.meta.getCodigoGeneracion();
            File folder = new File(determineFolderByNit(doc, true), folderName);

            File txtFile = new File(folder, folderName + ".txt");
            File pdfFile = new File(folder, folderName);

            // Generar el PDF
            DociStoragePayload payload = new DociStoragePayload();
            payload.setShorttoken(COMPANY_ID + doc.shorttoken);
            payload.setCompanyid(COMPANY_ID);
            payload.setAmbiente("00");
            payload.setTableName("docistorage");

            DociStorageResponse response = DociApiConsumer.getData(payload);
            if (response == null) {
                Log.error("Itemnum [{0}] estatus cambiado a [7]MISSING_API_DATA", doc.itemnum);
                Entries.updateEntry(doc.itemnum, 7);
                return; // Salir si no se obtuvo respuesta de la API
            }

            // Asegurar que la carpeta para el archivo esté creada
            pdfFile.mkdirs();

            // Generar el PDF
            DociPdfPayload pdfPayload = new DociPdfPayload(response, pdfFile.getAbsolutePath());
            DociApiConsumer.generatePdf(pdfPayload);

            // Generar el archivo de texto después de generar el PDF
            try {
                FileSystem.writeFile(txtFile, generateTxtContent(doc.meta, doc.json), StandardCharsets.UTF_8);
                Entries.updateEntry(doc.itemnum, 4);
                Log.log("Itemnum [{0}] estatus cambiado a [4]PROCESSED", doc.itemnum);
            } catch (IOException ex) {
                Entries.updateEntry(doc.itemnum, 5);
                Log.error("Itemnum [{0}] estatus cambiado a [5]PROBLEMATIC", doc.itemnum);
            }
        });
    }

    protected void startProcessing(String batch, Consumer<DocumentData> handler) {

        String query = "SELECT TOP  "
                + " FROM "
                + " WHERE "
                + " AND (" + batch + ") "
                + " ORDER BY DESC, ASC";

        Statements.executeSelect(query, (set) -> {
            String itemnum = "UNDEF";
            try {
                itemnum = set.getString("itemnum");
                String shorttoken = set.getString("shorttoken");
                String jsonPath = set.getString("json");
                

                DocumentMetadata meta = SerializerController.getMetadata(itemnum);
                if (meta == null) {
                    Entries.updateEntry(itemnum, 9);
                    Log.error("Itemnum [{0}] estatus cambiado a [9]MISSING_DB_DATA", itemnum);
                    return;
                }

                String jsonContent = SerializerController.getJson(new File(jsonPath));
                Json json = GSON.fromJson(jsonContent, Json.class);
                if (json == null) {
                    Entries.updateEntry(itemnum, 8);
                    Log.error("Itemnum [{0}] estatus cambiado a [8]MISSING_JSON", itemnum);
                    return;
                }

                // execute the 
                handler.accept(new DocumentData(itemnum, shorttoken, jsonPath, meta, json));
            } catch (SQLException ex) {
                Log.error(ex, "On SerializerJob#startProcessing (itemnum={0})", itemnum);
            }
        });
    }

// Inner class
    protected class DocumentData {

        protected DocumentData(String itemnum, String shorttoken, String jsonPath, DocumentMetadata meta, Json json) {
            this.itemnum = itemnum;
            this.shorttoken = shorttoken;
            this.jsonPath = jsonPath;
            this.meta = meta;
            this.json = json;
        }

        protected final String itemnum;
        protected final String shorttoken;
        protected final String jsonPath;

        protected final DocumentMetadata meta;
        protected final Json json;
    }

}