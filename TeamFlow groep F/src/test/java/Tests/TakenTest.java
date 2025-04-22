package Tests;

import Domein.Taken;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class TakenTest {

    @Test
    public void testTakenCreationWithoutGetters() throws NoSuchFieldException, IllegalAccessException {
        try {
            // Arrange: Create a Taken instance
            Taken taken = new Taken(1, 101, "Implement Feature", "Develop login system");


            // Act & Assert: Access private fields using reflection
            Field userStoryField = Taken.class.getDeclaredField("UserStory_idUserStory");
            userStoryField.setAccessible(true);
            assertEquals(1, userStoryField.getInt(taken), "User Story ID should match");

            Field taskIdField = Taken.class.getDeclaredField("idTaken");
            taskIdField.setAccessible(true);
            assertEquals(101, taskIdField.getInt(taken), "Task ID should match");

        } catch (SQLException e) {
            fail("SQLException should not occur during object creation: " + e.getMessage());
        }
    }
}