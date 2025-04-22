package Tests;

import Domein.Permissie;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class PermissieTest {

    @Test
    public void testPermissieCreationWithoutGetters() throws NoSuchFieldException, IllegalAccessException {
        // Arrange: Create a Permissie instance
        Permissie permissie = new Permissie(1, "Admin Access", "Read-Write", 1001, "TeamAlpha");

        // Act & Assert: Access private fields using reflection
        Field idField = Permissie.class.getDeclaredField("idPermissie");
        idField.setAccessible(true);
        assertEquals(1, idField.getInt(permissie), "ID should match");

        Field nameField = Permissie.class.getDeclaredField("permissieNaam");
        nameField.setAccessible(true);
        assertEquals("Admin Access", (String) nameField.get(permissie), "Name should match");

        Field typeField = Permissie.class.getDeclaredField("permissieType");
        typeField.setAccessible(true);
        assertEquals("Read-Write", (String) typeField.get(permissie), "Type should match");

        Field userIdField = Permissie.class.getDeclaredField("gebruikerHasTeam_gebruiker_idGebruiker");
        userIdField.setAccessible(true);
        assertEquals(1001, userIdField.getInt(permissie), "User ID should match");

        Field teamIdField = Permissie.class.getDeclaredField("gebruikerHasTeam_team_idTeam");
        teamIdField.setAccessible(true);
        assertEquals("TeamAlpha", (String) teamIdField.get(permissie), "Team ID should match");
    }
}