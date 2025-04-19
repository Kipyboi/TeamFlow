package Domein;

import Utils.DatabaseUtil;
import Utils.Session;

import java.sql.*;
import java.util.List;

import javax.xml.crypto.Data;
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

    public String getTeamNaam() {
        return teamNaam;
    }

    public void gebruikerToevoegen(Scanner scanner) throws SQLException {

        System.out.println("Typ de naam in van de gebruiker die je wilt toevoegen.");
        String gebruikersnaam = scanner.nextLine();
        Gebruiker gebruiker = null;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM gebruiker WHERE GebruikersNaam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gebruikersnaam);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                gebruiker = new Gebruiker(resultSet.getInt("idGebruiker"), resultSet.getString("gebruikersnaam"));

            }
            else {
                System.out.println("Gebruiker niet gevonden.");
                GebruikerBeheer(scanner);
            }
        }
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
        System.out.println("Gebruiker toegevoegd!");
        GebruikerBeheer(scanner);
    }

    public void gebruikerVerwijderen(Scanner scanner) throws SQLException {
        System.out.println("Typ de naam van de gebruiker die je wilt verwijderen.");
        String gebruikersnaam = scanner.nextLine();
        for (GebruikerHasTeam ght : gebruikers) {
            if (ght.getGebruiker().getGebruikersNaam().equals(gebruikersnaam)) {
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "DELETE FROM gebruiker_has_team WHERE gebruiker_idGebruiker = ? AND team_idteam = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, ght.getGebruiker().getIdGebruiker());
                    statement.setInt(2, this.idTeam);
                    statement.executeUpdate();
                }
                ght.getGebruiker().removeTeam(ght);
                gebruikers.remove(ght);
                System.out.println("Gebruiker verwijderd!");
                GebruikerBeheer(scanner);
            }

        }
    }

//    public void zoek (Scanner scanner) throws SQLException {
//        System.out.println("Typ hieronder de naam in van de epic die u zoekt");
//        String zoekterm = scanner.nextLine();
//        Team geselecteerdTeam = GeselecteerdTeamSession.getGeselecteerdTeam();
//        for (Epic epic : geselecteerdTeam.getEpics()) {
//            if (epic.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
//                System.out.println(epic.getScrumItemNaam());
//                System.out.println();
//            }
//        }
//        System.out.println("Typ de naam van de epic die u wilt bekijken.");
//        String epicNaam = scanner.nextLine();
//        for (Epic epic : geselecteerdTeam.getEpics()) {
//            if (epic.getScrumItemNaam().equalsIgnoreCase(epicNaam)) {
//                epic.menu(scanner);
//            }
//        }
//
//
//    }
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
        Main.Contextmenu(scanner);
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


        Epic epic = new Epic(idEpic, epicNaam, epicbeschrijving);
        this.epics.add(epic);

        System.out.println("Epic: " + epicNaam + " toegevoegd!");
        Main.Contextmenu(scanner);
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

    public void toonEpics(Scanner scanner) {
        System.out.println("Details van de Epics voor het team: " + this.getTeamNaam());
        ArrayList<Epic> epics = this.getEpics();

        if (epics == null || epics.isEmpty()) {
            System.out.println("Dit team heeft geen epics.");
            return;
        } else {
            int sum = 1;
            for (Epic epic : epics) {
                System.out.println("status: " + epic.getStatus());
                System.out.println("Epic " + sum + ". " + epic.getScrumItemNaam());
                sum++;
            }
            System.out.print("Typ het nummer van de Epic die je verder wilt bekijken: ");
            int keuze = -1;
            System.out.println("Wil je ook nog de status veranderen van deze Epic");
            try {
                keuze = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ongeldige invoer.");
                return;
            }

            if (keuze < 1 || keuze > epics.size()) {
                System.out.println("Ongeldige keuze, je moet wel een nummer kiezen die bij de Epic staat.");
                return;
            }
            Epic gekozenEpic = epics.get(keuze - 1);
            gekozenEpic.toonUserStories(scanner);
        }
    }

    public void toonBerichten(Scanner scanner) throws SQLException {
        List<Bericht> berichten = new ArrayList<>(); // Declare and initialize the list
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
            System.out.println(bericht.toString()); // Print the string representation of each Bericht
        }
        System.out.println("Typ posten om een nieuw bericht aan te maken of typ terug om terug te gaan");
        String keuze = scanner.nextLine();

        if (keuze.equalsIgnoreCase("posten")) {
            BerichtAanmaken(scanner);
        }
        else if (keuze.equalsIgnoreCase("terug")) {
            Main.Contextmenu(scanner);
        }
    }
    public String getName () {
        return teamNaam;
    }

    public void menu(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("\nTeam Menu: " + teamNaam);
            System.out.println("1. Navigeer naar een Epic");
            System.out.println("2. Toon berichten");
            System.out.println("3. Terug");
            System.out.println("4. Gebruikers beheren");
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
                    toonBerichten(scanner);
                case 3:
                    Main.gaTerug();
                    try {
                        Main.Contextmenu(scanner);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    return;
                case 4:
                    GebruikerBeheer(scanner);
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer opnieuw.");
            }
        }
    }
    
    private void navigeerNaarEpic(Scanner scanner) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Epic WHERE team_idteam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, this.idTeam);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int idEpic = resultSet.getInt("idEpic");
                String epicNaam = resultSet.getString("EpicNaam");
                String epicBeschrijving = resultSet.getString("EpicBeschrijving");
                Epic epic = new Epic(idEpic, epicNaam, epicBeschrijving);
                epics.add(epic);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (epics.isEmpty()) {
            System.out.println("Er zijn geen epics gekoppeld aan dit team.");
        }
    
        System.out.println("Beschikbare Epics:");
        for (Epic epic : epics) {
            System.out.println("- " + epic.getScrumItemNaam());
        }
    
        System.out.println("Typ de naam van de Epic die u wilt bekijken typ 'terug' om terug te gaan 'aanmaken' om een epic aan te maken 'verwijder' om een epic te verwijderen of typ de naam van een epic om naar de epic tegaan:");
        String keuze = scanner.nextLine();
    
        if (keuze.equalsIgnoreCase("terug")) {
            Main.Contextmenu(scanner);
            return;
        } else if (keuze.equalsIgnoreCase("aanmaken")) {
            // maak een nieuwe epic aan
            EpicAanmaken(scanner);
            return;
        } else if (keuze.equalsIgnoreCase("verwijder")) {
            // verwijder een epic
            EpicVerwijderen(scanner);
            return;
        } else {
            // ga naar de epic
            for (Epic epic : epics) {
                if (epic.getScrumItemNaam().equalsIgnoreCase(keuze)) {
                    Main.navigationStack.push(epic);
                    Main.Contextmenu(scanner);
                    return;
                }
            }
        }

    
        System.out.println("Epic niet gevonden. Probeer opnieuw.");
    }

    private void EpicVerwijderen(Scanner scanner) throws SQLException {
        System.out.println("Typ de naam van de epic die je wilt verwijderen");
        String epicNaam = scanner.nextLine();
        for (Epic epic : epics) {
            if (epic.getScrumItemNaam().equalsIgnoreCase(epicNaam)) {
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "DELETE FROM Epic WHERE idEpic = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, epic.getIdEpic());
                    statement.executeUpdate();
                }

                epics.remove(epic);
                System.out.println("Epic succesvol verwijderd");
                Main.Contextmenu(scanner);
            }
        }
        System.out.println("Epic niet gevonden. Probeer opnieuw.");
        Main.Contextmenu(scanner);
    }

    public void GebruikerBeheer(Scanner scanner) throws SQLException {
        for (GebruikerHasTeam gebruiker : gebruikers) {
            System.out.println("- " + gebruiker.getGebruiker().getGebruikersNaam());
        }
        System.out.println("Typ verwijder om een gebruiker te verwijderen typ toevoegen om een gebruiker toe te voegen of typ terug om terug te gaan");
        String keuze = scanner.nextLine();
        if (keuze.equalsIgnoreCase("terug")) {
            Main.Contextmenu(scanner);
            return;
        } else if (keuze.equalsIgnoreCase("verwijder")) {
            gebruikerVerwijderen(scanner);
            return;
        }else if (keuze.equalsIgnoreCase("toevoegen")) {
            gebruikerToevoegen(scanner);
            return;
        }else {
            System.out.println("Ongeldige keuze. Probeer opnieuw.");
            GebruikerBeheer(scanner);
            return;
        }


    }
}
