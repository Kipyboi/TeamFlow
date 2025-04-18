package Domein;

import Utils.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Utils.DatabaseUtil;
import java.sql.ResultSet;



public class Main {
    public static Stack<Object> navigationStack = new Stack<>();

    public static void main(String[] args) throws SQLException {
        GebruikerService inlogservice = new GebruikerService();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Wat is uw gebruikersnaam?");
        String gebruikersnaam = scanner.nextLine();
        while (!(inlogservice.gebruikerInloggen(gebruikersnaam))) {
            System.out.println("Ongeldige gebruikersnaam. Probeer het opnieuw.");
            System.out.println("Wat is uw gebruikersnaam?");
            gebruikersnaam = scanner.nextLine();
        }
        System.out.println("Succesvol ingelogd!");
        while (true) {
            if (navigationStack.isEmpty()) {
                toonHoofdMenu(scanner);
            } else {
                Object currentContext = navigationStack.peek();
                if (currentContext instanceof Team) {
                    ((Team) currentContext).menu(scanner);
                } else if (currentContext instanceof Epic) {
                    ((Epic) currentContext).menu(scanner);
                } else if (currentContext instanceof UserStory) {
                    ((UserStory) currentContext).menu(scanner);
                } else if (currentContext instanceof Taken) {
                    ((Taken) currentContext).menu(scanner);
                }
            }
        }
    }

    public static void toonHoofdMenu(Scanner scanner) throws SQLException {
        System.out.println("-- Welkom bij TeamFlow " + Session.getActiveGebruiker().getGebruikersNaam() + "! --");
        System.out.println("Kies een optie:");
        System.out.println("1. Naar teams");
        System.out.println("2. Nieuwe gebruiker aanmaken");
        System.out.println("3. Afsluiten");
        while (true) {
            int keuze = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (keuze) {
                case 1:
                    toonTeams(scanner);
                    break;
                case 2:
                    GebruikerService aanmaakService = new GebruikerService();
                    aanmaakService.gebruikerAanmaken(scanner);
                    break;
                case 3:
                    System.out.println("Programma afgesloten.");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Ongeldige keuze. Probeer het opnieuw.");
            }
        }
    }

    private static void toonTeams(Scanner scanner) throws SQLException {
        ArrayList<Team> teams = new ArrayList<>();
        try {
            for (GebruikerHasTeam ght : Session.getActiveGebruiker().getTeams()) {
                Team team = ght.getTeam();
                teams.add(team);
            }
        } catch (NullPointerException e) {
            System.out.println("Er zijn geen teams gekoppeld aan deze gebruiker.");
            //return;
        }


        System.out.println("Beschikbare teams:");
        for (Team team : teams) {
            System.out.println("- " + team.getName());
        }

        System.out.println("Typ de naam van het team dat u wilt bekijken of typ 'terug' om terug te gaan of typ 'aanmaak' om een team aan temaken:");
        String keuze = scanner.nextLine();

        if (keuze.equalsIgnoreCase("terug")) {
            toonHoofdMenu(scanner);
            return;
        }
        if (keuze.equalsIgnoreCase("aanmaak")) {
            maakTeamAan(scanner);
            return;
        }

        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(keuze)) {
                navigationStack.push(team);
                return;
            }
        }

        System.out.println("Team niet gevonden. Probeer opnieuw.");
    }

    public static void gaTerug() {
        if (!navigationStack.isEmpty()) {
            navigationStack.pop();
        }
    }
    public static void maakTeamAan(Scanner scanner) throws SQLException {
        int idTeam = -1;
        System.out.println("Voer de naam van het nieuwe team in: ");
        String teamNaam = scanner.nextLine();
        System.out.println("geef een beschrijving van het team: ");
        String teamBeschrijving = scanner.nextLine();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO team (teamNaam, teamBeschrijving) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, teamNaam);
            statement.setString(2, teamBeschrijving);
            statement.executeUpdate();
            System.out.println("Team aangemaakt!");
        } catch (SQLException e) {
            System.out.println("Fout bij het aanmaken van het team: " + e.getMessage());
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT idTeam FROM team WHERE teamNaam = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, teamNaam);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idTeam = resultSet.getInt("idTeam");
            } else {
                System.out.println("Team niet gevonden na aanmaken.");
            }
        } catch (SQLException e) {
            System.out.println("Fout bij het ophalen van het aangemaakte team: " + e.getMessage());
        }
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO gebruiker_has_team (gebruiker_idGebruiker, team_idteam) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(2, idTeam);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Fout bij het toevoegen van aangemaakte team " + e.getMessage());
        } finally {
            toonTeams(scanner);
        }


    }
}
