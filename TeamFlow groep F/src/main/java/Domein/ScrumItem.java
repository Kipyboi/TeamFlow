package Domein;

import Utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class ScrumItem{
    protected String scrumItemNaam;
    protected String ScrumItemBeschrijving;
    protected int idScrumItem;
    protected int status;
    protected ArrayList<GebruikerHasScrumItem> gebruikers;

    public ScrumItem(String scrumItemNaam, int idScrumItem) {
        this.scrumItemNaam = scrumItemNaam;
        this.idScrumItem = idScrumItem;
        this.status = 0;
        gebruikers = new ArrayList<>();
    }

    public ScrumItem(String scrumItemNaam, ScrumItemBeschrijving) {
        this.scrumItemNaam = scrumItemNaam;
        this.ScrumItemBeschrijving = ScrumItemBeschrijving;
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

    public String getScrumItemBeschrijving () {
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
