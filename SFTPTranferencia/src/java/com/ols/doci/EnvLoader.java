package com.ols.doci;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Singleton class to handle environment variables.
 *
 * @author Luis Brayan
 */
public class EnvLoader
{
    protected static boolean VERBOSE = false;
    protected static String FILE_PATH = null;
    protected static Consumer<Boolean> LISTENER = ((Boolean hasChanged) -> {
    });

    public static void setVerbose(boolean value)
    {
        VERBOSE = value;
    }

    protected static Document DOC;
    protected static long DOC_MODIFIED_AT = -1;
    protected static boolean DOC_HAS_CHANGED = true;
    protected static final HashMap<String, Object> CACHED_VALUES = new HashMap<>();

    public static boolean hasChanged()
    {
        return DOC_HAS_CHANGED;
    }

    /**
     * Initialize the configuration file loader.
     *
     * @param filePath on the tomcat folder. Something like
     * {@code env\config.xml}
     * @param printFile content that is been loaded
     * @param verbose shows the cache interactions
     * @param listener for when the file changes
     * @return whether the EnvLoader was initialized correctly.
     */
    public static boolean init(String filePath, boolean printFile, boolean verbose, Consumer<Boolean> listener)
    {
        String fullFilePath = System.getProperty("catalina.base")
                + File.separator + filePath;

        File configFile = new File(fullFilePath);
        if (configFile.exists()) {
            VERBOSE = verbose;
            FILE_PATH = fullFilePath;
            LISTENER = (listener != null) ? listener : ((Boolean hasChanged) -> {
            });

            Log.log("Using configFile at <{0}>", fullFilePath);
            if (printFile) {
                try {
                    Log.verbose("Content: <{0}>", Files.readString(configFile.toPath()));
                } catch (IOException ex) {
                    Log.error("ConfigFile could not be readed");
                }
            }
            return true;
        }
        return false;
    }

    public static boolean loadDoc()
    {
        if (FILE_PATH == null) {
            Log.error("Configuration file could not be located, run 'init' before 'loadDoc'.");
            return false;
        }

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            Log.error("Configuration file was not found at path: <{0}>", FILE_PATH);
            return false;
        }

        if (file.lastModified() == DOC_MODIFIED_AT) {
            return true;
        }

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            DOC = documentBuilder.parse(file);
            DOC_MODIFIED_AT = file.lastModified();

            if (VERBOSE) {
                Log.debug("Loaded Configuration file <{0}>", FILE_PATH);
            }
            return true;

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Log.error(ex, "On EnvLoader#loadDoc");
            return false;
        }
    }

    /**
     * If the configuration file has been updated, drops cached values and runs
     * any listener.
     */
    public static void refresh()
    {
        if (FILE_PATH == null) {
            Log.error("Configuration file could not be located, run 'init' before 'refresh'.");
            return;
        }

        long modifiedAt = DOC_MODIFIED_AT;
        if (!loadDoc()) {
            Log.error("Configuration file could not be loaded.");
            return;
        }

        if (modifiedAt == DOC_MODIFIED_AT) {
            if (VERBOSE) {
                Log.log("Configuration file has not changed, using cache");
            }
            DOC_HAS_CHANGED = false;
            LISTENER.accept(false);
            return;
        }

        // clear previous stored values
        CACHED_VALUES.clear();
        Log.log("Configuration file has changed, cache cleared");

        // reload values
        DOC_HAS_CHANGED = true;
        LISTENER.accept(true);
    }

    /**
     * Internal method to perform storing/resolving of values from in-memory
     * cache.
     *
     * @param <T>
     * @param key identifier of the value in the cache
     * @param clazz expected output class
     * @param fallback returned value it can not be resolved
     * @param getValue callback to perform a fresh read from the configuration
     * file
     * @return cached value.
     */
    protected static <T> T wrap(String key, Class<T> clazz, T fallback, Supplier<T> getValue)
    {
        // try to resolve a cached value
        Object cached = CACHED_VALUES.get(key);
        if (clazz.isInstance(cached)) {
            if (VERBOSE) {
                Log.verbose("Resolving <{0}> from cache with value <{1}>", key, cached);
            }
            return clazz.cast(cached);
        }

        // refresh the value
        T fresh = null;
        try {
            fresh = getValue.get();
        } catch (Exception ex) {
            Log.error(ex, "On EnvLoader#wrap");
        }

        // avoid resolving null
        if (fresh == null) {
            if (VERBOSE) {
                Log.verbose("Resolving <{0}> with fallback value <{1}>", key, fallback);
            }
            return fallback;
        }

        // resolve a new value
        CACHED_VALUES.put(key, fresh);
        if (VERBOSE) {
            Log.verbose("Resolving <{0}> from file with value <{1}>", key, fresh);
        }
        return fresh;
    }

    /**
     * Internal method to perform the reading of an attribute as String.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @param fallback returned value if the attribute is not found
     * @return extracted value
     */
    protected static String getValue(String tagname, String attribute, String fallback)
    {
        if (DOC == null) {
            return fallback;
        }
        try {
            NodeList elements = DOC.getElementsByTagName(tagname);

            // while runing this has throw errors
            int length = 0;
            try {
                length = elements.getLength();
            } catch (Exception ex) {
                Log.error("On EnvLoader#getLength searching for [{0}].[{1}]", tagname, attribute);
            }

            for (int i = 0; i < length; i++) {
                Node attr = elements.item(i).getAttributes().getNamedItem(attribute);
                if (attr == null) {
                    continue;
                }
                String value = attr.getNodeValue();
                if (value != null) {
                    return value;
                }
            }
        } catch (DOMException ex) {
            Log.error(ex, "On EnvLoader#getValue searching for [{0}].[{1}]", tagname, attribute);
        }
        return fallback;
    }

    /**
     * Extract a Boolean value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @return extracted value
     */
    public static Boolean getBoolean(String tagname, String attribute)
    {
        return getBoolean(tagname, attribute, false);
    }

    /**
     * Extract an Integer value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @return extracted value
     */
    public static Integer getInteger(String tagname, String attribute)
    {
        return getInteger(tagname, attribute, 0);
    }

    /**
     * Extract a Long value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @return extracted value
     */
    public static Long getLong(String tagname, String attribute)
    {
        return getLong(tagname, attribute, 0);
    }

    /**
     * Extract a Double value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @return extracted value
     */
    public static Double getDouble(String tagname, String attribute)
    {
        return getDouble(tagname, attribute, 0.0);
    }

    /**
     * Extract a String value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @return extracted value
     */
    public static String getString(String tagname, String attribute)
    {
        return getString(tagname, attribute, "");
    }

    /**
     * Extract a LogLevel from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @return extracted value
     */
    public static LogLevel getLogLevel(String tagname)
    {
        return wrap("[LogLevel]" + tagname + "@logLevel", LogLevel.class, LogLevel.LOG,
                () -> LogLevel.from(getValue(tagname, "logLevel", "log")));
    }

    /**
     * Extract a String value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @param fallback returned value if the attribute is not found
     * @return extracted value
     */
    public static String getString(String tagname, String attribute, String fallback)
    {
        return wrap("[String]" + tagname + "@" + attribute, String.class, fallback,
                () -> getValue(tagname, attribute, fallback));
    }

    /**
     * Extract a Boolean value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @param fallback returned value if the attribute is not found
     * @return extracted value
     */
    public static Boolean getBoolean(String tagname, String attribute, boolean fallback)
    {
        return wrap("[Boolean]" + tagname + "@" + attribute, Boolean.class, fallback, () -> {
            String value = getValue(tagname, attribute, "");
            return value.isEmpty() ? null : Boolean.valueOf(value);
        });
    }

    /**
     * Extract an Integer value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @param fallback returned value if the attribute is not found
     * @return extracted value
     */
    public static Integer getInteger(String tagname, String attribute, int fallback)
    {
        return wrap("[Integer]" + tagname + "@" + attribute, Integer.class, fallback, () -> {
            String value = getValue(tagname, attribute, "");
            return value.isEmpty() ? null : Integer.valueOf(value);
        });
    }

    /**
     * Extract a Long value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @param fallback returned value if the attribute is not found
     * @return extracted value
     */
    public static Long getLong(String tagname, String attribute, long fallback)
    {
        return wrap("[Long]" + tagname + "@" + attribute, Long.class, fallback, () -> {
            String value = getValue(tagname, attribute, "");
            return value.isEmpty() ? null : Long.valueOf(value);
        });
    }

    /**
     * Extract a Double value from the loaded configuration file.
     *
     * @param tagname XML element tagname
     * @param attribute name of the attribute on the XML element
     * @param fallback returned value if the attribute is not found
     * @return extracted value
     */
    public static Double getDouble(String tagname, String attribute, double fallback)
    {
        return wrap("[Double]" + tagname + "@" + attribute, Double.class, fallback, () -> {
            String value = getValue(tagname, attribute, "");
            return value.isEmpty() ? null : Double.valueOf(value);
        });
    }

    /**
     * Extract a List of values from the configuration file.
     *
     * @param tagname XML elements tagname
     * @param attribute attribute that contains the values
     * @param separator character used on the values string
     * @return extracted list of values
     */
    @SuppressWarnings("unchecked")
    public static List<String> getList(String tagname, String attribute, String separator)
    {
        return wrap("[List]" + tagname + "@" + attribute, List.class, Arrays.asList(), () -> {
            String value = getValue(tagname, attribute, "");
            return Arrays.asList(value.split(separator));
        });
    }

    /**
     * Extract a {@code key/value} Map from the configuration file.
     *
     * @param tagname XML elements tagname
     * @param keyAttribute attribute to be used as key
     * @param valueAttribute attribute to be used as value
     * @return extracted map of values
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> getMap(String tagname, String keyAttribute, String valueAttribute)
    {
        return wrap("[Map]" + tagname + "@" + keyAttribute + "_" + valueAttribute, HashMap.class, new HashMap<>(), () -> {
            if (DOC == null) {
                return null;
            }

            HashMap<String, String> map = new HashMap<>();
            try {
                NodeList elements = DOC.getElementsByTagName(tagname);
                for (int i = 0; i < elements.getLength(); i++) {
                    NamedNodeMap attrs = elements.item(i).getAttributes();
                    String key = attrs.getNamedItem(keyAttribute).getNodeValue();
                    String value = attrs.getNamedItem(valueAttribute).getNodeValue();
                    map.put(key, value);
                }
            } catch (NullPointerException ex) {
                // catches when the tagname is not found
            }
            return map;
        });
    }

    /**
     * Extracts a {@code key/value} Map from list nodes on the configuration
     * file.
     *
     * @param tagname XML elements tagname
     * @param keysAttribute attribute that contains the keys
     * @param valueAttribute attribute that contains the common value
     * @param separator character used on the keys string
     * @return extracted map of values
     */
    @SuppressWarnings("unchecked")
    public static HashMap<String, String> getMapFromLists(String tagname, String keysAttribute, String valueAttribute, String separator)
    {
        return wrap("[ListMap]" + tagname + "@" + keysAttribute + "_" + valueAttribute, HashMap.class, new HashMap<>(), () -> {
            if (DOC == null) {
                return null;
            }

            HashMap<String, String> map = new HashMap<>();
            try {
                NodeList elements = DOC.getElementsByTagName(tagname);
                for (int i = 0; i < elements.getLength(); i++) {
                    NamedNodeMap attrs = elements.item(i).getAttributes();
                    String value = attrs.getNamedItem(valueAttribute).getNodeValue();
                    for (String key : attrs.getNamedItem(keysAttribute).getNodeValue().split(separator)) {
                        map.put(key.trim(), value);
                    }
                }
            } catch (NullPointerException ex) {
                // catches when the tagname is not found
            }
            return map;
        });
    }
}
