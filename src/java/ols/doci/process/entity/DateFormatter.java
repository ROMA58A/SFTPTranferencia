package ols.doci.process.entity;

import com.ols.doci.Log;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author ols.brandon.mega
 */
public class DateFormatter {

    public static String formatoFhProcesamiento(String dateString) {

        String[] formatPatterns = {
            "yyyy-MM-dd",
            "dd/MM/yyyy",
            "yyyy/MM/dd"
        };

        String fecha = "";

        for (String pattern : formatPatterns) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(pattern);
            try {
                LocalDate parsedDate = LocalDate.parse(dateString, inputFormatter);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                fecha = parsedDate.format(outputFormatter);
                break;
            } catch (Exception e) {

            }
        }

        if (fecha.isEmpty()) {
            Log.log("La fecha no coincide con ninguno de los patrones proporcionados");
        }

        return fecha;
    }

    public static String FormatoFecha(Timestamp fecha) {
        if (fecha == null) {
            return " ";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return formatter.format(fecha.toLocalDateTime());
    }
}
