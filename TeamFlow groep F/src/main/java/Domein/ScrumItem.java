package Domein;

import Utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class ScrumItem{
    protected String scrumItemNaam;
    protected int idScrumItem;
    protected int status;
    protected String beschrijving;
    protected ArrayList<GebruikerHasScrumItem> gebruikers;

    public ScrumItem(String scrumItemNaam, int idScrumItem, String beschrijving) {
        this.scrumItemNaam = scrumItemNaam;
        this.idScrumItem = idScrumItem;
        this.status = 0;
        this.beschrijving = beschrijving;
        gebruikers = new ArrayList<>();
    }
    public ScrumItem (String scrumItemNaam, String beschrijving) {
        this.scrumItemNaam = scrumItemNaam;
        gebruikers = new ArrayList<>();
        this.status = 0;
        this.beschrijving = beschrijving;
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
    public abstract void gebruikerToewijzen (Gebruiker gebruiker) throws SQLException;
    public void gaNaar (Scanner scanner) {

    }
    public ArrayList<GebruikerHasScrumItem> getGebruikers () {
        return gebruikers;
    }




}
