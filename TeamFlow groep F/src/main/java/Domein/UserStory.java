package Domein;

import Utils.DatabaseUtil;
import Utils.Session;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

public class UserStory extends ScrumItem  implements IZoek, IMenu {
    private int idUserStory;
    private int Epic_idEpic;
    private ArrayList<Taken> taken;

    public UserStory(int idUserStory, int Epic_IdEpic, String scrumItemNaam, String beschrijving) throws SQLException {
        super(scrumItemNaam, beschrijving);
        this.idUserStory = idUserStory;
        this.Epic_idEpic = Epic_IdEpic;
    }


//    @Override
//    public void zoek(Scanner scanner) {
//        System.out.println("Typ hieronder de naam in van de taak die u zoekt");
//        String zoekterm = scanner.nextLine();
//        UserStory geselecteerdeUserStory = GeselecteerdeUserStorySession.getGeselecteerdeUserStory();
//        for (Taken t : geselecteerdeUserStory.getTaken()) {
//            if (t.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
//                System.out.println(t.getScrumItemNaam());
//                System.out.println();
//            }
//        }
//        System.out.println("Typ de naam van de epic die u wilt bekijken.");
//        String usNaam = scanner.nextLine();
//        for (Taken t : geselecteerdeUserStory.getTaken()) {
//            if (t.getScrumItemNaam().equalsIgnoreCase(usNaam)) {
//                t.gaNaar(scanner);
//            }
//        }
//
//    }


    @Override
    protected ArrayList<GebruikerHasScrumItem> checkToegewezen() throws SQLException {
        gebruikers = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM gebruiker_has_Userstory WHERE gebruiker_idGebruiker = ?";
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
            statement.setInt(2, this.idUserStory);
            statement.executeUpdate();
        }
        System.out.println("De User Story is succesvol aan u toegewezen.");
    }

    @Override
    public void TaakAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder de naam van de taak die je wilt aanmaken (enter om te versturen): ");
        String taakNaam = scanner.nextLine();
        System.out.println("Typ hieronder de beschrijving van " + taakNaam +" (enter om te versturen): ");
        String taakBeschrijving = scanner.nextLine();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Taken (TaakNaam, Userstory_idUserstory, TaakBeschrijving) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, taakNaam);
            statement.setInt(2, this.idUserStory);
            statement.setString(3, taakBeschrijving);

            statement.executeUpdate();
        }
        int idTaken = -1;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idTaken FROM Taken WHERE TaakNaam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, taakNaam);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idTaken = resultSet.getInt("idTaken");
            }
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO gebruiker_has_Taken (gebruiker_idGebruiker, Taken_idTaken) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(2, idTaken);
            statement.executeUpdate();
        }

        Taken taak = new Taken(this.idUserStory, idTaken, taakNaam, taakBeschrijving);
        this.taken.add(taak);

        System.out.println("Taak: " + taakNaam + " toegevoegd aan userstory:" + scrumItemNaam + "!");
    }

    public ArrayList<Taken> getTaken() {
        return taken;
    }

    public void toonBerichten(Scanner scanner) throws SQLException {
        List<Bericht> berichten = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Bericht_Userstory WHERE gebruiker_has_Userstory_Userstory_idUserstory = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.idUserStory);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Bericht bericht = new Bericht(resultSet.getInt("idBericht"), resultSet.getDate("datum"), resultSet.getString("berichtTekst"), resultSet.getInt("gebruiker_idGebruiker"), this);
                berichten.add(bericht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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


    public void menu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\nUser Story Menu: " + scrumItemNaam + ": " + beschrijving);
            System.out.println("1. Navigeer naar een Taak");
            System.out.println("2. Toon berichten");
            System.out.println("3. Gebruiker toewijzen aan userstory");
            System.out.println("3. Terug");
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
                    navigeerNaarTaak(scanner);
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

    private void navigeerNaarTaak(Scanner scanner) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Taken WHERE Userstory_idUserstory = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.idUserStory);
            ResultSet resultSet = statement.executeQuery();

            taken = new ArrayList<>();
            while (resultSet.next()) {
                int idTaken = resultSet.getInt("idTaken");
                String taakNaam = resultSet.getString("TaakNaam");
                String taakBeschrijving = resultSet.getString("TaakBeschrijving");
                Taken taak = new Taken(this.idUserStory, idTaken, taakNaam, taakBeschrijving);
                taken.add(taak);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (taken == null || taken.isEmpty()) {
            System.out.println("Er zijn geen taken gekoppeld aan deze user story.");
        }else {
            System.out.println("Beschikbare Taken:");
            for (Taken taak : taken) {
                System.out.println("- " + taak.getScrumItemNaam());
            }
        }

        System.out.println("Typ de naam van de Taak die u wilt bekijken typ 'terug' om terug tegaan typ 'verwijder' om een taak te verwijderen of typ 'aanmaken' om een taak aan te maken:");
        String keuze = scanner.nextLine();

        if (keuze.equalsIgnoreCase("terug")) {
            Main.Contextmenu(scanner);
            return;
        } else if (keuze.equalsIgnoreCase("verwijder")) {
//            taak verwijderen
            TaakVerwijderen(scanner);
            return;
        } else if (keuze.equalsIgnoreCase("aanmaken")) {
//            taak aanmaken
            TaakAanmaken(scanner);
            return;
        }else {
            for (Taken taak : taken) {
                if (taak.getScrumItemNaam().equalsIgnoreCase(keuze)) {
                    Main.navigationStack.push(taak);
                    Main.Contextmenu(scanner);
                    return;
                }
            }
        }



        System.out.println("Taak niet gevonden. Probeer opnieuw.");
    }

    private void TaakVerwijderen(Scanner scanner) throws SQLException {
        System.out.println("Typ de naam van de taak die je wilt verwijderen.");
        String usNaam = scanner.nextLine();
        for (Taken taak : taken) {
            if (taak.getScrumItemNaam().equalsIgnoreCase(usNaam)) {
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "DELETE FROM Taken WHERE idTaken = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, taak.getIdScrumItem());
                    statement.executeUpdate();
                }

                // ik hoop dat dit wel werkt
                taken.removeIf(e -> e.getScrumItemNaam().equals(usNaam));
//                taken.remove(taak);
                System.out.println("Taak succesvol verwijderd");
                Main.Contextmenu(scanner);
            }
        }
        System.out.println("Taak niet gevonden. Probeer opnieuw.");
        Main.Contextmenu(scanner);
    }

    public void toonTaken(Scanner scanner) {
        ArrayList<Taken> taken = this.getTaken();

        if (taken == null || taken.isEmpty()) {
            System.out.println("Deze User Story heeft nog geen taken.");
            return;
        }
        while (true) {
            System.out.println("Taken voor deze User Story:");
            int num = 1;
            for (Taken taak : taken) {
                System.out.println("Status: " + taak.getStatus());
                System.out.println(num + ". " + taak.getScrumItemNaam());
                System.out.println("   - " + taak.beschrijving);
                System.out.println();
                num++;
            }

            System.out.print("Wil je de status van de taak veranderen? (ja/nee): ");
            String antwoord = scanner.nextLine();

            if (antwoord.equalsIgnoreCase("ja")) {
                System.out.print("Typ het nummer van de taak waarvan je de status wilt veranderen: ");
                try {
                    int keuze = Integer.parseInt(scanner.nextLine());
                    if (keuze < 1 || keuze > taken.size()) {
                        System.out.println("Ongeldige keuze.");
                        return;
                    }

                    Taken gekozenTaak = taken.get(keuze - 1);
                    System.out.println("Huidige status van " + gekozenTaak.getScrumItemNaam() + ": " + gekozenTaak.getStatus());
                    System.out.println("Wat wordt de nieuwe status?");
                    System.out.println("0 = Open, 1 = Bezig, 2 = Klaar");

                    int nieuweStatus = Integer.parseInt(scanner.nextLine());
                    if (nieuweStatus < 0 || nieuweStatus > 2) {
                        System.out.println("Ongeldige status. Kies 0, 1 of 2.");
                        return;
                    }

                    gekozenTaak.setStatus(nieuweStatus);
                    System.out.println("Status succesvol aangepast naar: " + gekozenTaak.getStatus());

                } catch (NumberFormatException e) {
                    System.out.println("Ongeldige invoer. Gebruik een getal.");
                }
            } else {
                System.out.println("Terug naar het menu.");
                return;
            }
            System.out.println("Wil je nog een status aanpassen? (ja/nee)");
            String nogEen = scanner.nextLine();
            if (nogEen.equals("nee")) {
                break;
            }
        }
        System.out.println("Je gaat nu terug naar het menu");
        return;
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
                String query = "INSERT INTO Bericht_Userstory " +
                        "(tijdStamp, bericht, gebruiker_has_Userstory_gebruiker_idGebruiker, " +
                        "gebruiker_has_Userstory_Userstory_idUserstory)" +
                        " VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDate(1, sqlDate);
                statement.setString(2, berichtTekst);
                statement.setInt(3, Session.getActiveGebruiker().getIdGebruiker());
                statement.setInt(4, this.idUserStory);
                statement.executeUpdate();
            }

            Bericht bericht = new Bericht(-1, sqlDate, berichtTekst, Session.getActiveGebruiker().getIdGebruiker(), this);
            System.out.println("Bericht gepost!");
            Main.Contextmenu(scanner);
        }
        else {
            System.out.println("U bent niet toegewezen aan deze userstory en kan er daarom geen berichten over posten.");
            Main.Contextmenu(scanner);
        }
    }
    }


