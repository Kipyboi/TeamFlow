package Domein;

import Utils.DatabaseUtil;
import Utils.GeselecteerdTeamSession;
import Utils.Session;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    @Override
    public void EpicAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder de naam van de epic die je wilt aanmaken (enter om te versturen): ");
        String epicNaam = scanner.nextLine();
        System.out.println("Typ hieronder de beschrijving van " + epicNaam + " (enter om te versturen): ");
        String epicbeschrijving = scanner.nextLine();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Epic (EpicNaam, team_idteam, EpicBeschrijving) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, epicNaam);
            statement.setInt(2, this.idTeam);
            statement.setString(3, epicbeschrijving);

            statement.executeUpdate();
        }
        int idEpic = -1;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idEpic FROM Epic WHERE EpicNaam = ? AND team_idteam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, epicNaam);
            statement.setInt(2, this.idTeam);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idEpic = resultSet.getInt("idEpic");
            }
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Epic_has_gebruiker (gebruiker_idGebruiker, Epic_idEpic) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(2, idEpic);
            statement.executeUpdate();
        }


        // later op terug komen
        // waarom heeft epic geen beschijving en naam het heeft aleen een naam, maar in database heeft het beide???
        Epic epic = new Epic(idEpic, epicNaam, epicbeschrijving);
        this.epics.add(epic);

        System.out.println("Epic: " + epicNaam + " toegevoegd!");
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

    public void toonBerichten() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Bericht_Team WHERE gebruiker_has_team_team_idTeam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.idTeam);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBericht = resultSet.getInt("idBericht");
                Date tijdStamp = resultSet.getDate("tijdStamp");
                String berichtTekst = resultSet.getString("bericht");
                int gebruikerId = resultSet.getInt("gebruiker_has_team_gebruiker_idGebruiker");
                Bericht bericht = new Bericht(idBericht, tijdStamp, berichtTekst, gebruikerId, null);
                berichten.add(bericht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Bericht bericht : berichten) {
            bericht.toString();
        }
    }
    public String getName () {
        return teamNaam;
    }

    public void menu(Scanner scanner) {
        while (true) {
            System.out.println("\nTeam Menu: " + teamNaam);
            System.out.println("1. Navigeer naar een Epic");
            System.out.println("2. Terug");
            System.out.print("Kies een optie: ");
            int keuze;
    
            try {
                keuze = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ongeldige invoer. Voer een nummer in.");
                continue;
            }
    
            switch (keuze) {
                case 1:
                    navigeerNaarEpic(scanner);
                    break;
                case 2:
                    Main.gaTerug();
                    return;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }
    
    private void navigeerNaarEpic(Scanner scanner) {
        if (epics.isEmpty()) {
            System.out.println("Er zijn geen epics gekoppeld aan dit team.");
            return;
        }
    
        System.out.println("Beschikbare Epics:");
        for (Epic epic : epics) {
            System.out.println("- " + epic.getScrumItemNaam());
        }
    
        System.out.println("Typ de naam van de Epic die u wilt bekijken of typ 'terug' om terug te gaan:");
        String keuze = scanner.nextLine();
    
        if (keuze.equalsIgnoreCase("terug")) {
            return;
        }
    
        for (Epic epic : epics) {
            if (epic.getScrumItemNaam().equalsIgnoreCase(keuze)) {
                Main.navigationStack.push(epic);
                return;
            }
        }
    
        System.out.println("Epic niet gevonden. Probeer opnieuw.");
    }
}
