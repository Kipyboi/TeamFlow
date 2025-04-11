package Domein;

import java.util.ArrayList;

public abstract class ScrumItem{
    protected String scrumItemNaam;
    protected int idScrumItem;
    protected int status;
    protected ArrayList<GebruikerHasScrumItem> gebruikers;

    public ScrumItem(String scrumItemNaam, int idScrumItem) {
        this.scrumItemNaam = scrumItemNaam;
        this.idScrumItem = idScrumItem;
        this.status = 0;
        gebruikers = new ArrayList<>();
    }
    public ScrumItem (String scrumItemNaam) {
        this.scrumItemNaam = scrumItemNaam;
        gebruikers = new ArrayList<>();
        this.status = 0;
    }
    public String getScrumItemNaam () {
        return scrumItemNaam;
    }
    public int getStatus () {
        return status;
    }
    public int getIdScrumItem () {
        return idScrumItem;
    }



}
