package Utils;

import Domein.Epic;

public class GeselecteerdeEpicSession {
    private static Epic geselecteerdeEpic;

    public static Epic getGeselecteerdeEpic() {
        return geselecteerdeEpic;
    }

    public static void setGeselecteerdeEpic(Epic epic) {
        geselecteerdeEpic = epic;
    }
    public static void clear () {
        geselecteerdeEpic = null;
    }
}
