package Tests;

import Domein.GebruikerHasScrumItem;
import Domein.ScrumItem;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ScrumItemTest {
    class TestScrumItem extends ScrumItem {

        public TestScrumItem(String scrumItemNaam, String beschrijving) throws SQLException {
            super(scrumItemNaam, beschrijving);
        }

        @Override
        protected ArrayList<GebruikerHasScrumItem> checkToegewezen() {
            return new ArrayList<>();
        }

        @Override
        public void gebruikerToewijzen(Scanner scanner) {
            // Dummy implementation for testing
        }
    }

    @Test
    public void testGetScrumItemNaam() throws SQLException {
        ScrumItem testItem = new TestScrumItem("Login Feature", "Enhance security");

        assertEquals("Login Feature", testItem.getScrumItemNaam(), "Scrum item name should match");
    }
}
