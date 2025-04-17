package Domein;

import Utils.Session;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private static Stack<Object> navigationStack = new Stack<>();

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
                    return;
                default:
                    System.out.println("Ongeldige keuze. Probeer het opnieuw.");
            }
        }
    }

    private static void toonTeams(Scanner scanner) {
        ArrayList<GebruikerHasTeam> teams = Session.getActiveGebruiker().getTeams();
        if (teams.isEmpty()) {
            System.out.println("Er zijn geen teams gekoppeld aan deze gebruiker.");
            return;
        }

        System.out.println("Beschikbare teams:");
        for (GebruikerHasTeam team : teams) {
            System.out.println("- " + team.getTeam().getName());
        }

        System.out.println("Typ de naam van het team dat u wilt bekijken of typ 'terug' om terug te gaan:");
        String keuze = scanner.nextLine();

        if (keuze.equalsIgnoreCase("terug")) {
            return;
        }

        for (GebruikerHasTeam team : teams) {
            if (team.getTeam().getName().equalsIgnoreCase(keuze)) {
                navigationStack.push(team.getTeam());
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
}
