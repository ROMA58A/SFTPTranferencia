package com.ols.doci;

/**
 * @author Luis Brayan
 */
public enum LogLevel
{
    //  0,   1,     2,   3
    ERROR, LOG, DEBUG, VERBOSE;

    public boolean canDebug()
    {
        return this.ordinal() > 1;
    }

    public boolean canVerbose()
    {
        return this.ordinal() > 2;
    }

    public static LogLevel from(String source)
    {
        if (source.contains("verbose")) {
            return VERBOSE;
        } else if (source.contains("debug")) {
            return DEBUG;
        } else if (source.contains("log")) {
            return LOG;
        } else {
            return ERROR;
        }
    }
}
