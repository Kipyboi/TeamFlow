package Tests;

import Domein.GebruikerService;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class GebruikerServiceTest {

    @Test
    public void testGebruikerServiceInitialization() {
        try {
            // Act: Create an instance of GebruikerService
            GebruikerService gebruikerService = new GebruikerService();

            // Assert: Ensure the gebruikers list is initialized
            assertNotNull(gebruikerService.getGebruikers(), "Gebruikers list should not be null");

        } catch (SQLException e) {
            fail("GebruikerService constructor should not throw SQLException: " + e.getMessage());
        }
    }
}