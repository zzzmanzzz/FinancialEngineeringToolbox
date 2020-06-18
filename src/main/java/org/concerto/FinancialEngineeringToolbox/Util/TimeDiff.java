package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.Exception.DateFormatException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.time.temporal.ChronoUnit.DAYS;


public class TimeDiff {

    public static long getDaysBetweenDates(String early, String late) throws DateFormatException {
        final String format = "yyyy-MM-dd";
        DateTimeFormatter dtf =  DateTimeFormatter.ofPattern(format);
        LocalDate dateEarly;
        LocalDate dateLate;
        try {
            dateEarly = LocalDate.parse(early, dtf);
        } catch (DateTimeParseException e) {
            throw new DateFormatException(e.getMessage(), null);
        }

        try {
            dateLate = LocalDate.parse(late, dtf);
        } catch (DateTimeParseException e) {
            throw new DateFormatException(e.getMessage(), null);
        }
        return DAYS.between(dateEarly, dateLate);
    }
}
