package Domein;

import java.util.ArrayList;
import java.util.Scanner;

public class Gebruiker {
    private int idGebruiker;
    private String gebruikersNaam;
    private ArrayList<GebruikerHasTeam> teams;
    private ArrayList<GebruikerHasScrumItem> scrumItems;
    private ArrayList<Permissie> permissies;

    public Gebruiker (int idGebruiker, String gebruikersNaam) {
        this.idGebruiker = idGebruiker;
        this.gebruikersNaam = gebruikersNaam;
        this.teams = new ArrayList<>();
        this.scrumItems = new ArrayList<>();
    }

    public void AlleTeamsPrinten(Scanner scanner) {
        if (teams == null || teams.isEmpty()) {
            System.out.println("Er zijn op dit moment geen teams beschikbaar.");
            return;
        }

        System.out.println("Dit zijn alle teams uit het systeem:");
        int num = 1;
        for (GebruikerHasTeam ght : teams) {
            Team team = ght.getTeam();
            System.out.println("🐒 " + num + ". " + team.getTeamNaam());
            num++;
        }

        System.out.print("Type nu het nummer van het team dat je verder wilt bekijken: ");
        int keuze = -1;
        try {
            keuze = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ongeldige invoer. Voer een getal in.");
            return;
        }

        if (keuze < 1 || keuze > teams.size()) {
            System.out.println("Ongeldige keuze. Geen team met dit nummer.");
            return;
        }


        Team gekozenTeam = teams.get(keuze - 1).getTeam();

        gekozenTeam.toonEpics(scanner);
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

    public void addTeam(GebruikerHasTeam ght) {
        teams.add(ght);
    }
    public void removeTeam (GebruikerHasTeam ght) {
        teams.remove(ght);
    }
    public void addScrumItem (GebruikerHasScrumItem ghsi) {
        scrumItems.add(ghsi);
    }
    public void removeScrumItem(GebruikerHasScrumItem ghsi) {
        scrumItems.remove(ghsi);
    }
    public ArrayList<GebruikerHasScrumItem> getScrumItems() {
        return scrumItems;
    }
    public ArrayList<GebruikerHasTeam> getTeams() {
        return teams;
    }
}
