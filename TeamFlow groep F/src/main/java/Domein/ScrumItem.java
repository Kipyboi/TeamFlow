package Domein;

import Utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class ScrumItem{
    protected String scrumItemNaam;
    protected String beschrijving;
    protected ArrayList<GebruikerHasScrumItem> gebruikers;



    protected abstract ArrayList<GebruikerHasScrumItem> checkToegewezen() throws SQLException;

    public ScrumItem (String scrumItemNaam, String beschrijving) throws SQLException {
        this.scrumItemNaam = scrumItemNaam;
        gebruikers = new ArrayList<>();
        this.beschrijving = beschrijving;
        this.gebruikers = checkToegewezen();
    }
    public String getScrumItemNaam () {
        return scrumItemNaam;
    }
    public abstract void gebruikerToewijzen (Scanner scanner) throws SQLException;

}
