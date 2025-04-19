package Domein;

import Utils.DatabaseUtil;
import Utils.Session;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Taken extends ScrumItem implements IMenu {
    private int UserStory_idUserStory;
    private int idTaken;
    private int status;

    public Taken (int UserStory_idUserStory, int IdTaken, String scrumItemNaam, String beschrijving) throws SQLException {
        super(scrumItemNaam, beschrijving);
        this.UserStory_idUserStory = UserStory_idUserStory;
        this.idTaken = IdTaken;
    }


    @Override
    protected ArrayList<GebruikerHasScrumItem> checkToegewezen() throws SQLException {
        gebruikers = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM gebruiker_has_Taken WHERE gebruiker_idGebruiker = ?";
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



        @Override
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
                } else {
                    System.out.println("Gebruiker niet gevonden.");
                    Main.Contextmenu(scanner);
                }
            }
            GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(gebruiker, this);
            gebruiker.addScrumItem(ghsi);
            gebruikers.add(ghsi);
            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "INSERT INTO gebruiker_has_Taken (gebruiker_idGebruiker, Taken_idTaken) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, gebruiker.getIdGebruiker());
                statement.setInt(2, this.idTaken);
                statement.executeUpdate();
            }
            System.out.println("De taak is succesvol aan u toegewezen.");
        }

        public void menu (Scanner scanner) throws SQLException {
            while (true) {
                System.out.println("\nTaak Menu: " + scrumItemNaam + ": " + beschrijving);
                System.out.println("1. Toon berichten");
                System.out.println("2. Gebruiker toewijzen aan taak");
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
                        toonBerichten(scanner);
                        break;
                    case 2:
                        gebruikerToewijzen(scanner);
                    case 3:
                        Main.gaTerug();
                        Main.Contextmenu(scanner);
                        return;
                    default:
                        System.out.println("Ongeldige keuze. Probeer opnieuw.");
                }
            }
        }
        public void toonBerichten (Scanner scanner) throws SQLException {
            List<Bericht> berichten = new ArrayList<>(); // Declare and initialize the list
            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "SELECT * FROM Bericht_Taken WHERE gebruiker_has_Taken_Taken_idTaken = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, this.idTaken);
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
                System.out.println(bericht.toString()); // Print the string representation of each Bericht
            }
            System.out.println("Typ posten om een nieuw bericht aan te maken of typ terug om terug te gaan");
            String keuze = scanner.nextLine();

            if (keuze.equalsIgnoreCase("posten")) {
                BerichtAanmaken(scanner);
            } else if (keuze.equalsIgnoreCase("terug")) {
                Main.Contextmenu(scanner);
            }
        }
        @Override
        public void BerichtAanmaken (Scanner scanner) throws SQLException {
            boolean toegewezen = false;
            for (GebruikerHasScrumItem ghsi : Session.getActiveGebruiker().getScrumItems()) {
                ArrayList<GebruikerHasScrumItem> temp = ghsi.getScrumItem().gebruikers;
                for (GebruikerHasScrumItem ghsi2 : temp) {
                    if (ghsi2.getGebruiker() == Session.getActiveGebruiker()) {
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
                    String query = "INSERT INTO Bericht_Taken " +
                            "(tijdStamp, bericht, gebruiker_has_Taken_gebruiker_idGebruiker, " +
                            "gebruiker_has_Taken_Taken_idTaken)" +
                            " VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setDate(1, sqlDate);
                    statement.setString(2, berichtTekst);
                    statement.setInt(3, Session.getActiveGebruiker().getIdGebruiker());
                    statement.setInt(4, this.idTaken);
                    statement.executeUpdate();
                }

                Bericht bericht = new Bericht(-1, sqlDate, berichtTekst, Session.getActiveGebruiker().getIdGebruiker(), this);
                System.out.println("Bericht gepost!");
                Main.Contextmenu(scanner);
            } else {
                System.out.println("U bent niet toegewezen aan deze epic en kan er daarom geen berichten over posten.");
                Main.Contextmenu(scanner);
            }
        }

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
