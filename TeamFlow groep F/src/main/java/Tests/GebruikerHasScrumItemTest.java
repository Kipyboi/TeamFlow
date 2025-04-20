package Tests;

import Domein.Gebruiker;
import Domein.GebruikerHasScrumItem;
import Domein.ScrumItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;

class TestScrumItem extends ScrumItem {
    public TestScrumItem(String scrumItemNaam, String beschrijving) throws SQLException {
        super(scrumItemNaam, beschrijving);
        this.scrumItemNaam = scrumItemNaam;
        this.beschrijving = beschrijving;
    }

    @Override
    protected ArrayList<GebruikerHasScrumItem> checkToegewezen() {
        return new ArrayList<>();
    }

    @Override
    public void gebruikerToewijzen(java.util.Scanner scanner) {}
}

public class GebruikerHasScrumItemTest {

    @Test
    public void testGebruikerHasScrumItemCreation() throws SQLException {
        // Arrange: Create test objects
        Gebruiker gebruiker = new Gebruiker(1, "TestUser");
        ScrumItem scrumItem = new TestScrumItem("Login Feature", "Enhance security");

        // Act: Create GebruikerHasScrumItem
        GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(gebruiker, scrumItem);

        // Assert: Verify properties are correctly set
        assertEquals(gebruiker, ghsi.getGebruiker(), "Gebruiker should match the one provided");
        assertEquals(scrumItem, ghsi.getScrumItem(), "ScrumItem should match the one provided");
    }
}