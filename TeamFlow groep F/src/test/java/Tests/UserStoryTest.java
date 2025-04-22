package Tests;

import Domein.UserStory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

public class UserStoryTest {

    @Test
    public void testUserStoryCreation() {
        try {
            // Arrange: Create a UserStory instance
            UserStory userStory = new UserStory(1, 10, "Login Feature", "Implement secure authentication");


            // Act & Assert: Verify object is created successfully
            assertNotNull(userStory, "UserStory object should not be null");

        } catch (SQLException e) {
            fail("SQLException should not occur during object creation: " + e.getMessage());
        }
    }
}