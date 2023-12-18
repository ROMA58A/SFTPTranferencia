package ols.doci.process;

import com.ols.doci.ApiConnection;
import com.ols.doci.EnvLoader;
import com.ols.doci.Log;
import com.ols.doci.db.Database;
import com.ols.doci.job.CronIntervalUnits;
import com.ols.doci.job.CronScheduler;
import java.util.function.Consumer;
import javax.servlet.http.HttpServlet;
import ols.doci.connectorSFTP.SFTPConnector;
import ols.doci.process.pdf.DociApiConsumer;
import org.quartz.JobDetail;

/**
 * @author Luis Brayan
 */
public class StartService extends HttpServlet
{
    public static boolean IN_DEVELOPMENT = false;
    protected static int serializerInterval = 5;
    protected static int senderInterval = 1;

    @Override
    public void init()
    {
        try {
            Log.log("******* DOCI SFTP CUSCATLAN SERVICE *******");
            this.loadConfiguration();

            CronScheduler.init();
            JobDetail job1 = CronScheduler.prepareJob(SenderJob.class, "SFTP-SENDER");
            CronScheduler.forever(job1, senderInterval, CronIntervalUnits.MINUTES);

            JobDetail job2 = CronScheduler.prepareJob(SerializerJob.class, "SFTP-SERIALIZER");
            CronScheduler.forever(job2, serializerInterval, CronIntervalUnits.MINUTES);
            CronScheduler.start();

            Log.log("SFTP Service Initialized successfully");
        } catch (Exception ex) {
            Log.error(ex, "On service initialization");
        }
    }

    protected void loadConfiguration()
    {
        // is called when the EnvLoader#refresh is run
        Consumer<Boolean> listener = (Boolean hasChanged) -> {
            if (!hasChanged) {
                return;
            }

            Database.init();
            Database.initConnectionProperties();

            DociApiConsumer.init();

            SerializerJob.init();
            SenderJob.init();
        };

        // singletons inicialization
        Log.init("SFTP");
        EnvLoader.init("env/sftp.config.xml", false, true, listener);
        EnvLoader.loadDoc();

        // load global env
        IN_DEVELOPMENT = EnvLoader.getString("service", "mode").equals("dev");
        senderInterval = EnvLoader.getInteger("service", "senderIntervalInMinutes");
        serializerInterval = EnvLoader.getInteger("service", "serializerIntervalInMinutes");
        listener.accept(true);

        // other inicializations
        Database.ensureConnection();
        ApiConnection.init();
        SFTPConnector.init();

        // avoid printing more logs on producction
        EnvLoader.setVerbose(IN_DEVELOPMENT);
    }
}
