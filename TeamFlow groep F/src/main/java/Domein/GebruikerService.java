package Domein;

import Utils.DatabaseUtil;
import Utils.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public  void gebruikerAanmaken () {
        //shit
    }

    public boolean gebruikerInloggen (String gebruikersNaam) {
        for (Gebruiker gebruiker : gebruikers) {
            if (gebruiker.getGebruikersNaam().equals(gebruikersNaam)) {
                Session.setActieveGebruiker(gebruiker);
                return true;
            }
        }
        return false;
    }
}
