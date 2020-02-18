package org.concerto.FinancialEngineeringToolbox.Bond;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VanillaTest {

    @Test
    void getYTM() {
        Vanilla v = new Vanilla(1000000, 0.0125, 6, 99711);
        assertEquals(0.529975, v.getYTM(), 1e-5);
    }

    @Test
    void getInitialPrice() {
        Vanilla v = new Vanilla(1000, 0.08, 5, 0);
        assertEquals(924.1842646118307, v.getInitialPrice(0.1), 1e-5);
    }

    @Test
    void getMacaulayDuration() {
        Vanilla v = new Vanilla(1000, 0.1, 3, 1000);
        assertEquals(2.72867, v.getMacaulayDuration(0.12), 1e-5);
    }

    @Test
    void getModifiedDuration() {
        Vanilla v = new Vanilla(1000, 0.1, 3, 1000);
        assertEquals(2.43631, v.getModifiedDuration(0.12), 1e-5);
    }
}