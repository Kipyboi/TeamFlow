package Tests;

import Domein.Gebruiker;
import Domein.GebruikerHasTeam;
import Domein.GebruikerHasScrumItem;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GebruikerTest {

    @Test
    public void testGebruikerCreation() {
        // Arrange: Create a Gebruiker instance
        Gebruiker gebruiker = new Gebruiker(1, "TestUser");

        // Act & Assert: Verify attributes
        assertEquals(1, gebruiker.getIdGebruiker(), "Gebruiker ID should match");
        assertEquals("TestUser", gebruiker.getGebruikersNaam(), "GebruikersNaam should match");
    }

    @Test
    public void testGebruikerHasEmptyTeamsInitially() {
        // Arrange: Create a Gebruiker
        Gebruiker gebruiker = new Gebruiker(2, "NewUser");

        // Act & Assert: Ensure teams list starts empty
        assertNotNull(gebruiker.getTeams(), "Teams list should not be null");
        assertEquals(0, gebruiker.getTeams().size(), "Gebruiker should start with zero teams");
    }

    @Test
    public void testGebruikerHasEmptyScrumItemsInitially() {
        // Arrange: Create a Gebruiker
        Gebruiker gebruiker = new Gebruiker(3, "ScrumTester");

        // Act & Assert: Ensure scrumItems list starts empty
        assertNotNull(gebruiker.getScrumItems(), "ScrumItems list should not be null");
        assertEquals(0, gebruiker.getScrumItems().size(), "Gebruiker should start with zero ScrumItems");
    }
}