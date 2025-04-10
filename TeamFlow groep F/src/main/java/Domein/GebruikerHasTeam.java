package Domein;

import java.util.ArrayList;

public class GebruikerHasTeam {
    private int idGebruiker;
    private int idTeam;
    private ArrayList<Bericht> berichten;

    public GebruikerHasTeam(int idGebruiker, int idTeam) {
        this.idGebruiker = idGebruiker;
        this.idTeam = idTeam;
    }

}
