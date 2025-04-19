package Domein;

import java.util.ArrayList;

public class GebruikerHasScrumItem {
    private ScrumItem scrumItem;
    private Gebruiker gebruiker;
    private ArrayList<Bericht> berichten;

    public GebruikerHasScrumItem(Gebruiker gebruiker, ScrumItem scrumItem) {
        this.scrumItem = scrumItem;
        this.gebruiker = gebruiker;

    }

    public ScrumItem getScrumItem() {
        return scrumItem;
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }
}
