package Utils;

import Domein.Gebruiker;

public class Session {
    private static Gebruiker actieveGebruiker;

    public static void setActieveGebruiker(Gebruiker gebruiker) {
        actieveGebruiker = gebruiker;
    }
    public static Gebruiker getActiveGebruiker() {
        return actieveGebruiker;
    }
    public static void clear () {
        actieveGebruiker = null;
    }
}
