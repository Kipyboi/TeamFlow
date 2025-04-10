package Domein;

import java.util.ArrayList;

public class Team implements IZoek {
    private int IdTeam;
    private String TeamNaam;
    private ArrayList<GebruikerHasTeam> gebruikers;
    private ArrayList<ScrumItem> scrumItems;

    public Team (int IdTeam, String TeamNaam) {
        this.IdTeam = IdTeam;
        this.TeamNaam = TeamNaam;
        gebruikers = new ArrayList<>();
        scrumItems = new ArrayList<>();
    }



    public ScrumItem zoek () {

    }
}
