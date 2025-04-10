package Domein;

public class Permissie {
    private int idPermissie;
    private String permissieNaam;
    private String permissieType;
    private int gebruikerHasTeam_gebruiker_idGebruiker;
    private String gebruikerHasTeam_team_idTeam;

    public Permissie(int idPermissie, String permissieNaam, String permissieType, int gebruikerHasTeam_gebruiker_idGebruiker, String gebruikerHasTeam_team_idTeam) {
        this.idPermissie = idPermissie;
        this.permissieNaam = permissieNaam;
        this.permissieType = permissieType;
        this.gebruikerHasTeam_gebruiker_idGebruiker = gebruikerHasTeam_gebruiker_idGebruiker;
        this.gebruikerHasTeam_team_idTeam = gebruikerHasTeam_team_idTeam;
    }


    public void berichtAanmaken(){
        //maak aan
    }
}
