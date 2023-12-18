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
 * @author Luis Brayan
 */
public class SenderJob extends CronJob
{
    public static void init()
    {
        //LOCAL_DIRECTOY_IN = EnvLoader.getString("rutaArchivoDescarga", "ruta");
        //REMOTE_DIRECTORY_OUT = EnvLoader.getString("remoteDirectorySalida", "ruta");

        LOCAL_FOLDER_ACEPTED = EnvLoader.getString("filesystem", "localAceptedFolder");
        LOCAL_FOLDER_REJECTED = EnvLoader.getString("filesystem", "localRejectedFolder");

        REMOTE_FOLDER_ACEPTED = EnvLoader.getString("filesystem", "remoteAceptedFolder");
        REMOTE_FOLDER_REJECTED = EnvLoader.getString("filesystem", "remoteRejectedFolder");
        ENVIO_MINUTOS =EnvLoader.getInteger("uploadDelay", "Minutes", 1);
    }

    // local
    private static String LOCAL_FOLDER_ACEPTED;
    private static String LOCAL_FOLDER_REJECTED;
    // remote
    private static String REMOTE_FOLDER_ACEPTED;
    private static String REMOTE_FOLDER_REJECTED; 
    private static int ENVIO_MINUTOS ;

    @Override
    protected void main(JobExecutionContext context) throws Exception
    {
        SFTPConnector sftpConnector = new SFTPConnector();
        TransferenciaControllers CONTROLLER = new TransferenciaControllers();

        try {
            //CONTROLLER.descargarArchivos(REMOTE_DIRECTORY_OUT, LOCAL_DIRECTOY_IN, sftpConnector.connectToSftpServer());

            Log.log("Uploading REJECTED documents metadata");
            CONTROLLER.cargarcarpeta(LOCAL_FOLDER_REJECTED, REMOTE_FOLDER_REJECTED, ENVIO_MINUTOS, sftpConnector.connectToSftpServer());

            Log.log("Uploading ACEPTED documents metadata");
            CONTROLLER.cargarcarpeta(LOCAL_FOLDER_ACEPTED, REMOTE_FOLDER_ACEPTED , ENVIO_MINUTOS, sftpConnector.connectToSftpServer());

        } catch (IOException | ServletException e) {
            Log.error(e, "On SenderJob@main");
        } finally {
            if (sftpConnector.connectToSftpServer() != null && sftpConnector.connectToSftpServer().isConnected()) {
                sftpConnector.connectToSftpServer().disconnect();
                sftpConnector.disconnect();
            }
        }
    }
}
