package com.ols.doci;

import com.sun.istack.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.Function;

/**
 * @author Brandon
 */
public class FileSystem
{
    public static void init(boolean debug)
    {
        DEBUG = debug;
    }

    protected static boolean DEBUG = false;

    @Nullable
    public static boolean prepareFor(File file) throws IOException
    {
        File folder = file.getParentFile();
        if (!folder.exists()) {
            folder.mkdirs();
        }

        file.createNewFile();
        return file.exists();
    }

    public static void writeFile(File file, String content, Charset charset) throws IOException
    {
        prepareFor(file);
        if (DEBUG) {
            Log.debug("Writing file <{0}>", file.getPath());
        }

        try (FileWriter writer = new FileWriter(file, charset)) {
            writer.write(content);
            if (DEBUG) {
                Log.debug("The document was writed at <{0}>", file.getPath());
            }
        }
    }

    public static boolean outputStream(File file, Function<FileOutputStream, Boolean> handler)
    {
        try (FileOutputStream os = new FileOutputStream(file, false)) {
            prepareFor(file);
            if (DEBUG) {
                Log.debug("Writing file <{0}>", file.getPath());
            }

            if (handler.apply(os)) {
                if (DEBUG) {
                    Log.debug("The document was writed at <{0}>", file.getPath());
                }
                return true;
            }

        } catch (IOException ex) {
            Log.error(ex, "While writing file <{0}>", file.getPath());
        }
        return false;
    }
}
