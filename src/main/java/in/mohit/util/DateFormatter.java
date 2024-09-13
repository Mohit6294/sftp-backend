package in.mohit.util;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {
    public static LocalDateTime parseCustomDateString(String dateString) {
        // Define the pattern for the input string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

        // Parse the string into a ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, formatter);

        // Convert ZonedDateTime to LocalDateTime (if needed)
        return zonedDateTime.toLocalDateTime();
    }
}
