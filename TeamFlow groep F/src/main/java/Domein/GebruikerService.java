package Domein;

import Utils.DatabaseUtil;
import Utils.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import static Domein.Main.toonHoofdMenu;

public class GebruikerService {
    private ArrayList<Gebruiker> gebruikers;

    public ArrayList<Gebruiker> fillGebruikersList () throws SQLException {
        gebruikers = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM gebruiker";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Gebruiker gebruiker = new Gebruiker(resultSet.getInt("idGebruiker"), resultSet.getString("GebruikersNaam"));
                gebruikers.add(gebruiker);
            }
        }
        return gebruikers;
    }

    public GebruikerService() throws SQLException {
        gebruikers = fillGebruikersList();
    }

    public void gebruikerAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Voer de gebruikersnaam van de nieuwe gebruiker in: ");
        String gebruikersNaam = scanner.nextLine();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO gebruiker (gebruikersNaam) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, gebruikersNaam);
            statement.executeUpdate();
        }
        System.out.println("Nieuwe gebruiker aangemaakt!");
        toonHoofdMenu(scanner);
    }

    public boolean gebruikerInloggen (String gebruikersNaam) throws SQLException {
        for (Gebruiker gebruiker : gebruikers) {
            if (gebruiker.getGebruikersNaam().equals(gebruikersNaam)) {
                Session.setActieveGebruiker(gebruiker);
                Session.getActiveGebruiker().fillScrumItems();
                return true;
            }
        }
        return false;
    }
    public ArrayList<Gebruiker> getGebruikers () {
        return gebruikers;
    }
}
