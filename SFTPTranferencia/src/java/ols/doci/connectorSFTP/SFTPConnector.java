package ols.doci.connectorSFTP;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.ols.doci.EnvLoader;
import java.util.Properties;

/**
 *
 * @author ols.josue.mega
 */
public class SFTPConnector
{

    public static void init()
    {
        SFTP_HOST = EnvLoader.getString("sftp-credentials", "host");
        SFTP_PORT = EnvLoader.getInteger("sftp-credentials", "port");
        SFTP_USERNAME = EnvLoader.getString("sftp-credentials", "user");
        SFTP_PASSWORD = EnvLoader.getString("sftp-credentials", "password");

    }
    private Session session = null;
    private ChannelSftp channel = null;
    private static String SFTP_HOST;
    private static int SFTP_PORT;
    private static String SFTP_USERNAME;
    private static String SFTP_PASSWORD;

    public ChannelSftp connectToSftpServer()
    {

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(SFTP_USERNAME, SFTP_HOST, SFTP_PORT);
            session.setPassword(SFTP_PASSWORD);

            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();

            channel = (ChannelSftp) session.openChannel("sftp");

            return channel;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void disconnect()
    {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

}
