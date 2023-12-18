package ols.doci.controllers;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.ols.doci.Log;
import com.sun.istack.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import static ols.doci.process.StartService.IN_DEVELOPMENT;

/**
 * @author ols.josue.mega
 */
public class TransferenciaControllers {

    public void descargarArchivos(String sftpRutaArchivo, String rutaArchivoDescarga, ChannelSftp channel) throws ServletException, IOException, JSchException, SftpException {
        Log.verbose("descarga Inciada");

        channel.connect();
        channel.cd(sftpRutaArchivo);

        try {
            List<ChannelSftp.LsEntry> vector = channel.ls("*");
            for (ChannelSftp.LsEntry file : vector) {
                Log.verbose("Archivo creado <{0}>", file.getFilename());
                channel.get(file.getFilename(), rutaArchivoDescarga);
            }
        } catch (SftpException e) {
        }
    }

    public void cargarcarpeta(String rutaArchivoSubi, String remoteDirectory, int uploadDelayMinutes, ChannelSftp channel) throws ServletException, IOException {
        // Obtener la referencia al directorio local
        File localDirectory = new File(rutaArchivoSubi);

        try {
            channel.connect();
            channel.cd(remoteDirectory);
            Log.log("Ruta del directorio carpeta <{0}> ", localDirectory);

            // Verificar si el directorio existe
            if (localDirectory.exists() && localDirectory.isDirectory()) {
                REMOTE_ROOT_PATH = remoteDirectory;
                subirCarpetas(channel, localDirectory, uploadDelayMinutes);
            }
        } catch (JSchException | SftpException e) {
        }
    }

    protected static String REMOTE_ROOT_PATH = "";

    public static void subirCarpetas(ChannelSftp channel, File directorio, long uploadDelayMinutes) {
        // Obtener la lista de carpetas en el directorio
        File[] carpetas = directorio.listFiles(File::isDirectory);

        // Recorrer las carpetas y subirlas al directorio remoto
        if (carpetas == null || carpetas.length < 1) {
            Log.verbose("No se encontraron carpetas dentro del directorio");
            return;
        }

        // Comparar la fecha del archivo con la del sistema
        long currentTimeMillis = System.currentTimeMillis();
        for (File localFolder : carpetas) {
            String localPath = localFolder.getName();

            long timeDifferenceMinutes = (currentTimeMillis - localFolder.lastModified()) / (60 * 1000);
            if (timeDifferenceMinutes < uploadDelayMinutes) {
                // No ha pasado suficiente tiempo, omitir el envío
                if (IN_DEVELOPMENT) {
                    Log.error("Local folder <{0}> upload delayed.", localPath);
                }
                continue;
            }
            // check local folder content
            File[] localFiles = localFolder.listFiles();
            if (localFiles.length < 1) {
                if (IN_DEVELOPMENT) {
                    Log.error("Local folder <{0}> is incomplete. Upload delayed.", localPath);
                }
                continue;
            }

            // try uploading folder and its content
            String remoteFolder = uploadFolder(channel, localFolder, localFiles);
            if (remoteFolder == null) {
                // keep problematic folders for more tries
                Log.error("Local folder <{0}> could not be uploaded.", localPath);
                continue;
            }
            Log.log("Local folder <{0}> uploaded into <{1}>", localPath, remoteFolder);

            // Eliminar la carpeta del directorio localmente
            localFolder.delete();
            if (IN_DEVELOPMENT) {
                Log.debug("Local folder <{0}> removed.", localPath);
            }

        }
    }

    @Nullable
    public static String uploadFolder(ChannelSftp remote, File localFolder, File[] localFiles) {
        try {
            remote.cd(REMOTE_ROOT_PATH);
        } catch (SftpException ex) {
            if (IN_DEVELOPMENT) {
                Log.error("Pointer could not be moved to root folder <{0}>. <exception: {1}>", REMOTE_ROOT_PATH, ex.getMessage());
            }
            return null;
        }

        String remotePath = remote.pwd() + "/" + localFolder.getName();
        if (IN_DEVELOPMENT) {
            Log.debug("Uploading <{0}> to <{1}>", localFolder.getName(), remotePath);
        }

        // clear previous remote folder
        //try {
        //    if (remote.ls(remotePath).size() < 1) {
        //        remote.rmdir(remotePath);
        //    }
        //} catch (SftpException ex) {
        //    if (IN_DEVELOPMENT) {
        //        Log.error("Remote folder <{0}> could not be removed. <exception: {1}>", remotePath, ex.getMessage());
        //    }
        //    return null;
        //}
        //
        // prepare remote folder
        try {
            remote.mkdir(remotePath);
        } catch (SftpException ex) {
            if (IN_DEVELOPMENT) {
                Log.error("Remote folder <{0}> could not be created. <exception: {1}>", remotePath, ex.getMessage());
            }
            //return null;
        }

        // move into remote folder
        try {
            remote.cd(remotePath);
        } catch (SftpException ex) {
            if (IN_DEVELOPMENT) {
                Log.error("Pointer could not be moved  into remote folder <{0}>. <exception: {1}>", remotePath, ex.getMessage());
            }
            return null;
        }

        // upload content of the folder
        if (!uploadFolderContent(remote, localFiles)) {
            if (IN_DEVELOPMENT) {
                Log.error("Some local files on <{0}> could not be uploaded.", remotePath);
            }
            return null;
        }

        return remotePath;
    }

    public static boolean uploadFolderContent(ChannelSftp remote, File[] localFiles) {
        for (File localFile : localFiles) {
            if (!localFile.isFile()) {
                continue;
            }

            String filename = localFile.getName();
            if (IN_DEVELOPMENT) {
                Log.log("Uploading file <{0}>", filename);
            }

            // upload local file
            try (FileInputStream fis = new FileInputStream(localFile.getAbsolutePath())) {
                remote.put(fis, localFile.getName());
            } catch (IOException | SftpException ex) {
                if (IN_DEVELOPMENT) {
                    Log.error("Local file <{0}> could not be uploaded. <exception: {1}>", filename, ex.getMessage());
                }
                return false;
            }

            if (IN_DEVELOPMENT) {
                Log.debug("Local file <{0}> uploaded.", filename);
            }
        }

        for (File localFile : localFiles) {
            // remove local file
            localFile.delete();
            if (IN_DEVELOPMENT) {
                Log.debug("Local file <{0}> removed.", localFile.getName());
            }
        }

        return true;
    }
}
