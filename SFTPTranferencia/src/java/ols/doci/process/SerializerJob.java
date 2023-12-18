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
 * @author Luis Brayan
 */
public class SerializerJob extends CronJob
{
    public static void init()
    {
        SHOULD_RUN = EnvLoader.getBoolean("jobs", "serializerShouldRun");

        COMPANY_ID = EnvLoader.getString("companyId", "id");
        LOCAL_FOLDER_ACEPTED = EnvLoader.getString("filesystem", "localAceptedFolder");
        LOCAL_FOLDER_REJECTED = EnvLoader.getString("filesystem", "localRejectedFolder");
    }

    protected static boolean SHOULD_RUN;

    private static String COMPANY_ID;
    private static String LOCAL_FOLDER_ACEPTED;
    private static String LOCAL_FOLDER_REJECTED;

    @Override
    protected boolean hasWorkToDo(JobExecutionContext context)
    {
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
    protected void main(JobExecutionContext context) throws Exception
    {
        GSON = new Gson();

        processRejectedBatch();
        processAceptedBatch();
    }

    public Gson GSON;

    protected void processRejectedBatch()
    {
        if (rejectedBatchSize < 1) {
            return;
        }

        startProcessing(rejectedBatch, (doc) -> {
            Log.log("***** procesando rejected itemnum <{0}>", doc.itemnum);

            // Nombre de la carpeta basado en el código de generación
            String folderName = doc.meta.getCodigoGeneracion();
            File folder = new File(LOCAL_FOLDER_REJECTED, folderName);

            File txtFile = new File(folder, folderName + ".txt");
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

    protected void processAceptedBatch()
    {
        if (aceptedBatchSize < 1) {
            return;
        }

        startProcessing(aceptedBatch, (doc) -> {
            Log.log("***** procesando acepted itemnum <{0}>", doc.itemnum);

            // Nombre de la carpeta basado en el código de generación
            String folderName = doc.meta.getCodigoGeneracion();
            File folder = new File(LOCAL_FOLDER_ACEPTED, folderName);
            File txtFile = new File(folder, folderName + ".txt");
            File pdfFile = new File(folder, folderName);

            // generate the pdf
            DociStoragePayload payload = new DociStoragePayload();
            payload.setShorttoken(COMPANY_ID + doc.shorttoken);
            payload.setCompanyid(COMPANY_ID);
            payload.setAmbiente("00");
            payload.setTableName("docistorage");

            DociStorageResponse response = DociApiConsumer.getData(payload);
            if (response == null) {
                Entries.updateEntry(doc.itemnum, 7);
                Log.error("Itemnum [{0}] estatus cambiado a [7]MISSING_API_DATA", doc.itemnum);
                return;
            }

            // ensure the folder for the file is created, to prevent problems
            pdfFile.mkdirs();

            // generate pdf
            DociPdfPayload pdfPayload = new DociPdfPayload(response, pdfFile.getAbsolutePath());
            DociApiConsumer.generatePdf(pdfPayload);

            // generate the txt after if the pdf was generated
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

    protected void startProcessing(String batch, Consumer<DocumentData> handler)
    {
        String query = "SELECT"
              
                + " WHERE "
                + ") ORDER BY  DESC";

        Statements.executeSelect(query, (set) -> {
            // catchs exceptions on a row level
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

    // inner class
    protected class DocumentData
    {
        protected DocumentData(String itemnum, String shorttoken, String jsonPath, DocumentMetadata meta, Json json)
        {
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
