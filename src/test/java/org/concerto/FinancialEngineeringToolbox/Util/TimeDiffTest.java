package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.Exception.DateFormatException;

import static org.junit.jupiter.api.Assertions.*;

class TimeDiffTest {

    @org.junit.jupiter.api.Test
    void betweenDates() throws DateFormatException {
        String early = "2000-01-01";
        String late = "2000-12-31";
        long diff = TimeDiff.getDaysBetweenDates(early, late);
        assertEquals(365, diff);
    }

    @org.junit.jupiter.api.Test
    void formatError() {
        String early = "2000/01/01";
        String late = "2000-12-31";
        assertThrows(DateFormatException.class, ()->TimeDiff.getDaysBetweenDates(early, late));
    }


}