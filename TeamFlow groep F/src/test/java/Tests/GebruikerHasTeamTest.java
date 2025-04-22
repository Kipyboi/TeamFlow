package Tests;

import Domein.Gebruiker;
import Domein.Team;
import Domein.GebruikerHasTeam;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GebruikerHasTeamTest {

    @Test
    public void testGebruikerHasTeamCreation() {
        // Arrange: Create test objects
        Gebruiker gebruiker = new Gebruiker(1, "TestUser");
        Team team = new Team(5, "Development Team");

        // Act: Create GebruikerHasTeam instance
        GebruikerHasTeam ght = new GebruikerHasTeam(gebruiker, team);

        // Assert: Verify properties
        assertEquals(gebruiker, ght.getGebruiker(), "Gebruiker should match");
        assertEquals(team, ght.getTeam(), "Team should match");
    }

    @Test
    public void testSetGebruiker() {
        // Arrange: Create test objects
        Gebruiker gebruiker1 = new Gebruiker(1, "OriginalUser");
        Gebruiker gebruiker2 = new Gebruiker(2, "NewUser");
        Team team = new Team(5, "QA Team");
        GebruikerHasTeam ght = new GebruikerHasTeam(gebruiker1, team);

        // Act: Change the gebruiker
        ght.setGebruiker(gebruiker2);

        // Assert: Verify the change
        assertEquals(gebruiker2, ght.getGebruiker(), "Gebruiker should be updated");
    }
}