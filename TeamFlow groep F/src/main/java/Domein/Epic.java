package Domein;

import Utils.DatabaseUtil;
import Utils.Session;
import java.util.List;
import java.sql.ResultSet;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import static Domein.Main.navigationStack;

public class Epic extends ScrumItem  implements IZoek, IMenu {
    private int idEpic;
    ArrayList<UserStory> UserStories;

    public Epic (int idEpic, String scrumItemNaam, String beschrijving) throws SQLException {
        super(scrumItemNaam, beschrijving);
        this.idEpic = idEpic;
    }
    @Override
    public void zoek (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder de naam in van de userstory die u zoekt");
        String zoekterm = scanner.nextLine();
        boolean gevonden = false;

        for (UserStory userStory : UserStories) {
            if (userStory.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                System.out.println("Userstory:" + userStory.getScrumItemNaam());
                System.out.println();
                gevonden = true;
            }
        }
        if(!gevonden) {
            System.out.println("Userstory is niet gevonden.");
            return;
        }
        System.out.println("Typ de naam van de userstory die u wilt bekijken.");
        String epicNaam = scanner.nextLine();
        boolean epicGevonden = false;


        for (UserStory userStory : UserStories) {
            if (userStory.getScrumItemNaam().equalsIgnoreCase(epicNaam)) {
                navigationStack.push(userStory);
                Main.Contextmenu(scanner);
                epicGevonden = true;
                break;
            }
        }
        if (!epicGevonden) {
            System.out.println("Geen geldige epic met deze naam gevonden");
            Main.Contextmenu(scanner);
        }
    }

    @Override
    protected ArrayList<GebruikerHasScrumItem> checkToegewezen() throws SQLException{
        gebruikers = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Epic_has_gebruiker WHERE gebruiker_idGebruiker = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(Session.getActiveGebruiker(), this);
                gebruikers.add(ghsi);
            }
        }
        return gebruikers;
    }

    public void gebruikerToewijzen (Scanner scanner) throws SQLException {
        Gebruiker gebruiker = null;
        System.out.println("Typ de naam in van de gebruiker die je wilt toewijzen: ");
        String naam = scanner.nextLine();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM gebruiker WHERE GebruikersNaam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, naam);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                gebruiker = new Gebruiker(resultSet.getInt("idGebruiker"), resultSet.getString("GebruikersNaam"));
            }
            else {
                System.out.println("Gebruiker niet gevonden.");
                Main.Contextmenu(scanner);
            }
        }
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
        System.out.println("De Epic is succesvol toegewezen aan: " + naam);
        Main.Contextmenu(scanner);
    }
    @Override
    public void BerichtAanmaken (Scanner scanner) throws SQLException {
        boolean toegewezen = false;
        for (GebruikerHasScrumItem ghsi : Session.getActiveGebruiker().getScrumItems()) {
            ArrayList<GebruikerHasScrumItem> temp = ghsi.getScrumItem().gebruikers;
            for (GebruikerHasScrumItem ghsi2 : temp) {
                if(ghsi2.getGebruiker() == Session.getActiveGebruiker()){
                    toegewezen = true;
                }
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
            Main.Contextmenu(scanner);
        }
        else {
            System.out.println("U bent niet toegewezen aan deze epic en kan er daarom geen berichten over posten.");
           Main.Contextmenu(scanner);
        }
    }

    @Override
    public void UserstoryAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder de naam van de userstory die je wilt aanmaken (enter om te versturen): ");
        String userstoryNaam = scanner.nextLine();
        System.out.println("Typ hieronder de beschrijving van " + userstoryNaam + " (enter om te versturen): ");
        String userstorybeschrijving = scanner.nextLine();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Userstory (UserStoryNaam, Epic_idEpic, UserStoryBeschrijving) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, userstoryNaam);
            statement.setInt(2, this.idEpic);
            statement.setString(3, userstorybeschrijving);

            statement.executeUpdate();
        }
        int idUserStory = -1;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idUserStory FROM Userstory WHERE UserStoryNaam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userstoryNaam);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idUserStory = resultSet.getInt("idUserStory");
            }
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO gebruiker_has_Userstory (gebruiker_idGebruiker, Userstory_idUserstory) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(2, idUserStory);
            statement.executeUpdate();
        }

        UserStory userstory = new UserStory(idUserStory,  this.idEpic, userstoryNaam, userstorybeschrijving);
        this.UserStories.add(userstory);

        System.out.println("Userstory: " + userstoryNaam + " toegevoegd aan epic: " + scrumItemNaam + "!");
        Main.Contextmenu(scanner);
    }

    public ArrayList<UserStory> getUserStories() {
        return UserStories;
    }

    public void toonBerichten(Scanner scanner) throws SQLException {
        List<Bericht> berichten = new ArrayList<>();
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
            System.out.println(bericht.toString());
        }

        System.out.println("-- Typ posten om een nieuw bericht aan te maken of typ terug om terug te gaan --");
        String keuze = scanner.nextLine();

        if (keuze.equalsIgnoreCase("posten")) {
            BerichtAanmaken(scanner);
        }
        else if (keuze.equalsIgnoreCase("terug")) {
            Main.Contextmenu(scanner);
        }


    }
    @Override
    public void menu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\nEpic Menu: " + scrumItemNaam + ": " + beschrijving);
            System.out.println("1. Navigeer naar een User Story");
            System.out.println("2. Toon berichten");
            System.out.println("3. Gebruiker toewijzen aan epic");
            System.out.println("4. Terug");
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
                    toonBerichten(scanner);
                    break;
                case 3:
                    gebruikerToewijzen(scanner);
                case 4:
                    Main.gaTerug();
                    Main.Contextmenu(scanner);
                    return;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }

    private void navigeerNaarUserStory(Scanner scanner) throws SQLException {
        try(Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Userstory WHERE Epic_idEpic = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.idEpic);
            ResultSet resultSet = statement.executeQuery();

            UserStories = new ArrayList<>();
            while (resultSet.next()) {
                int idUserStory = resultSet.getInt("idUserStory");
                String naam = resultSet.getString("UserStoryNaam");
                String beschrijving = resultSet.getString("UserStoryBeschrijving");
                UserStories.add(new UserStory(idUserStory, this.idEpic, naam, beschrijving));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (UserStories.isEmpty()) {
            System.out.println("Er zijn geen user stories gekoppeld aan deze epic.");
        }

        System.out.println("Beschikbare User Stories:");
        for (UserStory userStory : UserStories) {
            System.out.println("- " + userStory.getScrumItemNaam());
        }

        System.out.println("Typ 'verwijder' om een Userstory te verwijderen type 'aanmaken' om een userstory aan temaken typ 'terug' om terug tegaan of typ de naam van een userstory om naar de userstory tegaan:");
        System.out.println("of typ zoek om een userstory te zoeken");
        String keuze = scanner.nextLine();

        if (keuze.equalsIgnoreCase("terug")) {
            Main.Contextmenu(scanner);
            return;
        } else if (keuze.equalsIgnoreCase("verwijder")) {
//        verwijder aan maken
            VerwijderUserStory(scanner);
            navigeerNaarUserStory(scanner);

        }else if (keuze.equalsIgnoreCase("aanmaken")) {
                // Userstory aanmaken
            UserstoryAanmaken(scanner);
            navigeerNaarUserStory(scanner);
            return;
        }
        else if (keuze.equalsIgnoreCase("zoek")) {
            zoek(scanner);
            return;
        }
        else {
            for (UserStory userStory : UserStories) {
                if (userStory.getScrumItemNaam().equalsIgnoreCase(keuze)) {
                    navigationStack.push(userStory);
                    Main.Contextmenu(scanner);
                    return;
                } else {
                    System.out.println("User Story niet gevonden. Probeer opnieuw.");
                }
            }
        }



        System.out.println("User Story niet gevonden. Probeer opnieuw.");
    }

    private void VerwijderUserStory(Scanner scanner) throws SQLException {
        System.out.println("Typ de naam van de userstory die je wilt verwijderen");
        String usNaam = scanner.nextLine();
        for (UserStory userStory : UserStories) {
            if (userStory.getScrumItemNaam().equalsIgnoreCase(usNaam)) {
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "DELETE FROM Userstory WHERE idUserstory = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, userStory.getIdUserStory());
                    statement.executeUpdate();
                }

                UserStories.remove(userStory);
                System.out.println("Userstory succesvol verwijderd");
                Main.Contextmenu(scanner);
            }
        }
        System.out.println("Userstory niet gevonden. Probeer opnieuw.");
        Main.Contextmenu(scanner);
    }

    public int getIdEpic() {
        return idEpic;
    }
}
