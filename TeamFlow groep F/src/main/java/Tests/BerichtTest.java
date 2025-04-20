package Tests;

import Domein.Bericht;
import org.junit.jupiter.api.Test;
import java.sql.Date;
import static org.junit.jupiter.api.Assertions.*;

class BerichtTest {

    @Test
    void testToString() {
        Date tijdStamp = new Date(System.currentTimeMillis());
        String berichtText = "Testbericht";
        int idGebruiker = 1;

        Bericht bericht = new Bericht(1, tijdStamp, berichtText, idGebruiker, null);

        String expectedOutput = tijdStamp.toString() + "\nOnbekend\n" + berichtText;
        assertEquals(expectedOutput, bericht.toString());
    }
}