package com.ols.doci;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class to perform printing of information in the console.
 *
 * @author Brandon
 */
public class Log
{
    protected static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    protected static String MODULE = "SCHEDULE";

    public static void init(String module)
    {
        MODULE = module;
    }

    public static void error(String pattern, Object... arguments)
    {
        Log.error(MessageFormat.format(pattern, arguments));
    }

    public static void log(String pattern, Object... arguments)
    {
        Log.log(MessageFormat.format(pattern, arguments));
    }

    public static void debug(String pattern, Object... arguments)
    {
        Log.debug(MessageFormat.format(pattern, arguments));
    }

    public static void verbose(String pattern, Object... arguments)
    {
        Log.verbose(MessageFormat.format(pattern, arguments));
    }

    public static void error(String message)
    {
        System.out.println(MessageFormat.format("[{0} {1}] ERROR: {2}",
                Log.now(), MODULE, message));
    }

    public static void log(String message)
    {
        System.out.println(MessageFormat.format("[{0} {1}] LOG: {2}",
                Log.now(), MODULE, message));
    }

    public static void debug(String message)
    {
        System.out.println(MessageFormat.format("[{0} {1}] DEBUG: {2}",
                Log.now(), MODULE, message));
    }

    public static void verbose(String message)
    {
        System.out.println(MessageFormat.format("[{0} {1}] VERBOSE: {2}",
                Log.now(), MODULE, message));
    }

    public static void error(Exception ex, String pattern, Object... arguments)
    {
        Log.error(ex, MessageFormat.format(pattern, arguments));
    }

    public static void error(Exception ex, String message)
    {
        String msg = MessageFormat.format("[{0} {1}] ERROR({3}): {2} <{4}>",
                Log.now(), MODULE,
                message, ex.getClass(), ex.getMessage());
        System.out.println(msg);

        // dispatch the exception to the main Logger
        Logger.getLogger(MODULE).log(Level.SEVERE, message, ex);
    }

    protected static String now()
    {
        return DTF.format(LocalDateTime.now());
    }
}
