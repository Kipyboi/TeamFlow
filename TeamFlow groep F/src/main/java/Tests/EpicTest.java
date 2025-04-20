package Tests;

import Domein.Epic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    @Test
    public void testEpicCreation() {
        Epic epic = new Epic(1, "Sprint Planning");

        assertEquals(1, epic.getIdEpic());
        assertEquals("Sprint Planning", epic.getScrumItemNaam());
    }
}