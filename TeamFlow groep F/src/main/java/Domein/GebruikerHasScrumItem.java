package Domein;

import java.util.ArrayList;

public class GebruikerHasScrumItem {
    private int idScrumItem;
    private int idGebruiker;
    private ArrayList<Bericht> berichten;

    public GebruikerHasScrumItem(int idScrumItem, int idGebruiker) {
        this.idScrumItem = idScrumItem;
        this.idGebruiker = idGebruiker;

    }
}
