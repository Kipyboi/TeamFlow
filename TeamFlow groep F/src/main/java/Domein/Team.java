package Domein;

import Utils.DatabaseUtil;
import Utils.GeselecteerdTeamSession;
import Utils.Session;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Team implements IZoek, IMenu {
    private int idTeam;
    private String teamNaam;
    private ArrayList<GebruikerHasTeam> gebruikers;
    private ArrayList<Epic> epics;

    public Team (int idTeam, String TeamNaam) {
        this.idTeam = idTeam;
        this.teamNaam = TeamNaam;
        gebruikers = new ArrayList<>();
        epics = new ArrayList<>();
    }
    public void gebruikerToevoegen(Gebruiker gebruiker) throws SQLException {
        GebruikerHasTeam ght = new GebruikerHasTeam(gebruiker, this);
        gebruiker.addTeam(ght);
        gebruikers.add(ght);
        try (Connection connection = DatabaseUtil.getConnection()){
            String query = "INSERT INTO gebruiker_has_team (gebruiker_idGebruiker, team_idteam) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gebruiker.getIdGebruiker());
            statement.setInt(2, this.idTeam);
            statement.executeUpdate();

        }
    }

    public void gebruikerVerwijderen(Gebruiker gebruiker, String gebruikersnaam) throws SQLException {
        for (GebruikerHasTeam ght : gebruikers) {
            if (ght.getGebruiker().getGebruikersNaam().equals(gebruikersnaam)) {
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "DELETE FROM gebruiker_has_team WHERE gebruiker_idGebruiker = ? AND team_idteam = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, gebruiker.getIdGebruiker());
                    statement.setInt(2, this.idTeam);
                    statement.executeUpdate();
                }
                ght.getGebruiker().removeTeam(ght);
                gebruikers.remove(ght);
            }

        }
    }



    public void zoek (Scanner scanner) {
        System.out.println("Typ hieronder de naam in van de epic die u zoekt");
        String zoekterm = scanner.nextLine();
        Team geselecteerdTeam = GeselecteerdTeamSession.getGeselecteerdTeam();
        for (Epic epic : geselecteerdTeam.getEpics()) {
            if (epic.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                System.out.println(epic.getScrumItemNaam());
                System.out.println();
            }
        }
        System.out.println("Typ de naam van de epic die u wilt bekijken.");
        String epicNaam = scanner.nextLine();
        for (Epic epic : geselecteerdTeam.getEpics()) {
            if (epic.getScrumItemNaam().equalsIgnoreCase(epicNaam)) {
                epic.gaNaar(scanner);
            }
        }


    }
    public void BerichtAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder het bericht dat je wilt posten (enter om te versturen): ");
        String berichtTekst = scanner.nextLine();

        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Bericht_Team " +
                    "(tijdStamp, bericht, gebruiker_has_team_gebruiker_idGebruiker, " +
                    "gebruiker_has_team_team_idTeam)" +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, sqlDate);
            statement.setString(2, berichtTekst);
            statement.setInt(3, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(4, this.idTeam);
            statement.executeUpdate();
        }

        Bericht bericht = new Bericht(-1, sqlDate, berichtTekst, Session.getActiveGebruiker().getIdGebruiker(), null);
        System.out.println("Bericht aangemaakt!");
        GeselecteerdTeamSession.getGeselecteerdTeam().gaNaar(scanner);
    }
    public void gaNaar (Scanner scanner) {

    }

    public ArrayList<GebruikerHasTeam> getGebruikers() {
        return gebruikers;
    }

    public void setGebruikers(ArrayList<GebruikerHasTeam> gebruikers) {
        this.gebruikers = gebruikers;
    }

    public ArrayList<Epic> getEpics() {
        return epics;
    }
}
