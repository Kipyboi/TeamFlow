package Tests;

import Domein.Team;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    @Test
    public void testTeamCreation() {
        // Arrange: Create a Team instance
        Team team = new Team(1, "Development Team");

        // Act & Assert: Verify attributes
        assertNotNull(team, "Team object should not be null");
    }
}