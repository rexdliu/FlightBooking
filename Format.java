import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Format {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Method to parse a date from string
    public static Date parseDate(String dateString) throws ParseException {
        return dateFormat.parse(dateString);
    }

    // Method to format a date to string
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
}
