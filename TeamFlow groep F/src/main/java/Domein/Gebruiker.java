package Domein;

import java.util.ArrayList;

public class Gebruiker {
    private int idGebruiker;
    private String gebruikersNaam;
    private ArrayList<GebruikerHasTeam> Teams;
    private ArrayList<GebruikerHasScrumItem> scrumItems;
    private ArrayList<Permissie> permissies;

    public Gebruiker (int idGebruiker, String gebruikersNaam) {
        this.idGebruiker = idGebruiker;
        this.gebruikersNaam = gebruikersNaam;
    }

    public String getGebruikersNaam() {
        return gebruikersNaam;
    }

    public void setGebruikersNaam(String gebruikersNaam) {
        this.gebruikersNaam = gebruikersNaam;
    }

    public int getIdGebruiker() {
        return idGebruiker;
    }

    public void setIdGebruiker(int idGebruiker) {
        this.idGebruiker = idGebruiker;
    }
}
