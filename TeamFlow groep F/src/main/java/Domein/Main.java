package Domein;

import Utils.Session;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
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
        toonHoofdMenu(scanner);

    }

    public static void toonHoofdMenu(Scanner scanner) throws SQLException {

            System.out.println("-- Welkom bij TeamFlow " + Session.getActiveGebruiker().getGebruikersNaam() + "! --");
            System.out.println("Kies een optie:");
            System.out.println("1. Naar teams");
            System.out.println("2. Nieuwe gebruiker aanmaken");
            System.out.println("3. Afsluiten");
        while (true) {
            int keuze = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (keuze) {
                case 1:
                    openTeams();
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

    private static void openTeams() {
        // Implementatie voor het openen van teams
        System.out.println("Teams geopend.");
    }
}
