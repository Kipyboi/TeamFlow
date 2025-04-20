package Tests;

import Domein.Epic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class EpicTest {

    @Test
    public void testEpicCreation() {
        try {
            // Arrange: Create an Epic instance
            Epic epic = new Epic(1, "Sprint Planning", "Plan tasks for the sprint");

            // Act & Assert: Verify object creation
            assertNotNull(epic, "Epic object should not be null");

        } catch (SQLException e) {
            fail("SQLException should not occur during object creation: " + e.getMessage());
        }
    }
}