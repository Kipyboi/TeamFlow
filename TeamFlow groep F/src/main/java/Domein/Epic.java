package Domein;

import Utils.DatabaseUtil;
import Utils.GeselecteerdTeamSession;
import Utils.GeselecteerdeEpicSession;
import Utils.Session;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Epic extends ScrumItem  implements IZoek, IMenu {
    private int idEpic;
    ArrayList<UserStory> UserStories;

    public Epic (int idEpic, String scrumItemNaam, String beschrijving) {
        super(scrumItemNaam, beschrijving);
        this.idEpic = idEpic;
    }
    public Epic (String scrumItemNaam, String beschrijving) {
        super(scrumItemNaam, beschrijving);
    }

    @Override
    public void zoek(Scanner scanner) {
        System.out.println("Typ hieronder de naam in van de user story die u zoekt");
        String zoekterm = scanner.nextLine();
        Epic geselecteerdeEpic = GeselecteerdeEpicSession.getGeselecteerdeEpic();
        for (UserStory us : geselecteerdeEpic.getUserStories()) {
            if (us.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                System.out.println(us.getScrumItemNaam());
                System.out.println();
            }
        }
        System.out.println("Typ de naam van de epic die u wilt bekijken.");
        String usNaam = scanner.nextLine();
        for (UserStory us : geselecteerdeEpic.getUserStories()) {
            if (us.getScrumItemNaam().equalsIgnoreCase(usNaam)) {
                us.gaNaar(scanner);
            }
        }

    }
    public void gebruikerToewijzen (Gebruiker gebruiker) throws SQLException {
        GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(gebruiker, this);
        gebruiker.addScrumItem(ghsi);
        gebruikers.add(ghsi);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Epic_has_gebruiker (gebruiker_idGebruiker, Epic_idEpic) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gebruiker.getIdGebruiker());
            statement.setInt(2, this.idEpic);
            statement.executeUpdate();
        }
        System.out.println("De Epic is succesvol aan u toegewezen.");
    }
    @Override
    public void BerichtAanmaken (Scanner scanner) throws SQLException {
        boolean toegewezen = false;
        for (GebruikerHasScrumItem ghsi : Session.getActiveGebruiker().getScrumItems()) {
            if (ghsi.getScrumItem().getGebruikers().contains(Session.getActiveGebruiker())) {
                toegewezen = true;
            }
        }

        if (toegewezen) {
            System.out.println("Typ hieronder het bericht dat je wilt posten (enter om te versturen): ");
            String berichtTekst = scanner.nextLine();

            LocalDate currentDate = LocalDate.now();
            Date sqlDate = Date.valueOf(currentDate);

            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "INSERT INTO Bericht_Epic " +
                        "(tijdStamp, bericht, Epic_has_gebruiker_gebruiker_idGebruiker, " +
                        "Epic_has_gebruiker_Epic_idEpic)" +
                        " VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDate(1, sqlDate);
                statement.setString(2, berichtTekst);
                statement.setInt(3, Session.getActiveGebruiker().getIdGebruiker());
                statement.setInt(4, this.idEpic);
                statement.executeUpdate();
            }

            Bericht bericht = new Bericht(-1, sqlDate, berichtTekst, Session.getActiveGebruiker().getIdGebruiker(), this);
            System.out.println("Bericht gepost!");
            GeselecteerdeEpicSession.getGeselecteerdeEpic().gaNaar(scanner);
        }
        else {
            System.out.println("U bent niet toegewezen aan deze epic en kan er daarom geen berichten over posten.");
            GeselecteerdeEpicSession.getGeselecteerdeEpic().gaNaar(scanner);
        }
    }

    @Override
    public void UserstoryAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder de naam van de userstory die je wilt aanmaken (enter om te versturen): ");
        String userstoryNaam = scanner.nextLine();
        System.out.println("Typ hieronder de beschrijving van " + userstoryNaam +" (enter om te versturen): ");
        String userstorybeschrijving = scanner.nextLine();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Userstory (UserStoryNaam, Epic_idEpic, UserStoryBeschrijving) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, userstoryNaam);
            statement.setInt(2, this.idEpic);
            statement.setString(3, userstorybeschrijving);

            statement.executeUpdate();
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idUserStory FROM Userstory WHERE UserStoryNaam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userstoryNaam);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                this.idUserStory = resultSet.getInt("idUserStory");
            }
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO gebruiker_has_Userstory (gebruiker_idGebruiker, Userstory_idUserstory) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(2, this.idUserStory);
            statement.executeUpdate();
        }

        UserStory userstory = new UserStory (userstoryNaam, userstorybeschrijving);
        this.UserStories.add(userstory);

        System.out.println("Userstory: " + userstoryNaam + " toegevoegd aan epic:" + scrumItemNaam + "!");
    }

    public ArrayList<UserStory> getUserStories() {
        return UserStories;
    }

    public void toonBerichten() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Bericht_Epic WHERE Epic_has_gebruiker_Epic_idEpic = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.idEpic);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int idBericht = resultSet.getInt("idBericht");
                Date tijdStamp = resultSet.getDate("tijdStamp");
                String bericht = resultSet.getString("bericht");
                int gebruikerId = resultSet.getInt("Epic_has_gebruiker_gebruiker_idGebruiker");
                Bericht berichtObject = new Bericht(idBericht, tijdStamp, bericht, gebruikerId, this);
                berichten.add(berichtObject);
            }
        } catch (SQLException e) {

        }

        for (Bericht bericht : berichten) {
            bericht.toString();
        }

    }

    public void menu(Scanner scanner) {
        while (true) {
            System.out.println("\nEpic Menu: " + scrumItemNaam);
            System.out.println("1. Navigeer naar een User Story");
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
                    navigeerNaarUserStory(scanner);
                    break;
                case 2:
                    Main.gaTerug();
                    return;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }

    private void navigeerNaarUserStory(Scanner scanner) {
        if (UserStories.isEmpty()) {
            System.out.println("Er zijn geen user stories gekoppeld aan deze epic.");
            return;
        }

        System.out.println("Beschikbare User Stories:");
        for (UserStory userStory : UserStories) {
            System.out.println("- " + userStory.getScrumItemNaam());
        }

        System.out.println("Typ de naam van de User Story die u wilt bekijken of typ 'terug' om terug te gaan:");
        String keuze = scanner.nextLine();

        if (keuze.equalsIgnoreCase("terug")) {
            return;
        }

        for (UserStory userStory : UserStories) {
            if (userStory.getScrumItemNaam().equalsIgnoreCase(keuze)) {
                Main.navigationStack.push(userStory);
                return;
            }
        }

        System.out.println("User Story niet gevonden. Probeer opnieuw.");
    }

}
