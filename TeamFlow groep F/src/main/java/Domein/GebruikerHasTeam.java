package Domein;

import java.util.ArrayList;

public class GebruikerHasTeam {
    private Gebruiker gebruiker;
    private Team team;
    private ArrayList<Bericht> berichten;

    public GebruikerHasTeam(Gebruiker gebruiker, Team team) {
        this.gebruiker = gebruiker;
        this.team = team;
        berichten = new ArrayList<>();
    }

    public Gebruiker getGebruiker() {
        return gebruiker;
    }

    public void setGebruiker(Gebruiker gebruiker) {
        this.gebruiker = gebruiker;
    }
    public Team getTeam() {
        return team;
    }
    public void setTeam(Team team) {
        this.team = team;
    }
}
