package ols.doci.process.entity;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * @author ols.brandon.mega
 */
public class DateFormatter
{
    public static String FormatoFecha(String fecha)
    {
        if (fecha.isEmpty()) {
            return " ";
        }

        DateTimeFormatter current = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return formatter.format(current.parse(fecha));
    }

    public static String FormatoFecha(Timestamp fecha)
    {
        if (fecha == null) {
            return " ";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return formatter.format(fecha.toLocalDateTime());
    }
}
