package ols.doci.process;

import com.ols.doci.EnvLoader;
import com.ols.doci.Log;
import com.ols.doci.job.CronJob;
import java.io.IOException;
import javax.servlet.ServletException;
import ols.doci.connectorSFTP.SFTPConnector;
import ols.doci.controllers.TransferenciaControllers;
import org.quartz.JobExecutionContext;

/**
 * @author Brandon
 */
public class SenderJob extends CronJob {

    private static String LOCAL_FOLDER_ACEPTED;
    private static String LOCAL_FOLDER_REJECTED;
    private static String REMOTE_FOLDER_ACEPTED;
    private static String REMOTE_FOLDER_REJECTED;
    private static int ENVIO_MINUTOS;

    private static String LOCAL_FOLDER_ACEPTEDBac;
    private static String LOCAL_FOLDER_REJECTEDBac;
    private static String REMOTE_FOLDER_ACEPTEDBac;
    private static String REMOTE_FOLDER_REJECTEDBac;

    public static void init() {
        LOCAL_FOLDER_ACEPTED = EnvLoader.getString("filesystem", "localAceptedFolder");
        LOCAL_FOLDER_REJECTED = EnvLoader.getString("filesystem", "localRejectedFolder");
        REMOTE_FOLDER_ACEPTED = EnvLoader.getString("filesystem", "remoteAceptedFolder");
        REMOTE_FOLDER_REJECTED = EnvLoader.getString("filesystem", "remoteRejectedFolder");
        ENVIO_MINUTOS = EnvLoader.getInteger("uploadDelay", "Minutes", 1);
        LOCAL_FOLDER_ACEPTEDBac = EnvLoader.getString("filesystem", "localAceptedFolderBac");
        LOCAL_FOLDER_REJECTEDBac = EnvLoader.getString("filesystem", "localRejectedFolderBac");
        REMOTE_FOLDER_ACEPTEDBac = EnvLoader.getString("filesystem", "remoteAceptedFolderBac");
        REMOTE_FOLDER_REJECTEDBac = EnvLoader.getString("filesystem", "remoteRejectedFolderBac");
    }

    @Override
    protected void main(JobExecutionContext context) throws Exception {

        init();

        SFTPConnector sftpConnector = new SFTPConnector();
        TransferenciaControllers CONTROLLER = new TransferenciaControllers();

        try {
            // Llama al método cargarCarpeta con los parámetros adecuados
            CONTROLLER.cargarcarpeta(LOCAL_FOLDER_REJECTED, REMOTE_FOLDER_REJECTED, ENVIO_MINUTOS, sftpConnector.connectToSftpServer());
            CONTROLLER.cargarcarpeta(LOCAL_FOLDER_ACEPTED, REMOTE_FOLDER_ACEPTED, ENVIO_MINUTOS, sftpConnector.connectToSftpServer());
            CONTROLLER.cargarcarpeta(LOCAL_FOLDER_REJECTEDBac, REMOTE_FOLDER_REJECTEDBac, ENVIO_MINUTOS, sftpConnector.connectToSftpServer());
            CONTROLLER.cargarcarpeta(LOCAL_FOLDER_ACEPTEDBac, REMOTE_FOLDER_ACEPTEDBac, ENVIO_MINUTOS, sftpConnector.connectToSftpServer());

            // También puedes llamar a descargarArchivos si es necesario
            // CONTROLLER.descargarArchivos(rutaSFTPRemota, rutaArchivoDescarga, channel);
        } catch (ServletException e) {
            Log.error(e, "En SenderJob@main");
        } finally {
            if (sftpConnector.connectToSftpServer() != null && sftpConnector.connectToSftpServer().isConnected()) {
                sftpConnector.connectToSftpServer().disconnect();
                sftpConnector.disconnect();
            }

        }
    }
}
