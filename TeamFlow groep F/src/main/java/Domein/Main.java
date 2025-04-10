package Domein;

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
        System.out.println("Welkom " + gebruikersnaam + "! Typ '/help' voor een lijst met commandos");

    }
}
