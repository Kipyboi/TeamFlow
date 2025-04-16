package Domein;

import Utils.DatabaseUtil;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Team implements IZoek {
    private int idTeam;
    private String teamNaam;
    private ArrayList<GebruikerHasTeam> gebruikers;
    private ArrayList<ScrumItem> scrumItems;

    public Team(int idTeam, String TeamNaam) throws SQLException {
        this.idTeam = idTeam;
        this.teamNaam = TeamNaam;
        gebruikers = new ArrayList<>();
        scrumItems = new ArrayList<>();
        voegTeamToe(TeamNaam);
    }

    public void gebruikerToevoegen(Gebruiker gebruiker) throws SQLException {
        GebruikerHasTeam ght = new GebruikerHasTeam(gebruiker, this);
        gebruiker.addTeam(ght);
        gebruikers.add(ght);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO gebruiker_has_team (gebruiker_idGebruiker, team_idteam) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gebruiker.getIdGebruiker());
            statement.setInt(2, this.idTeam);
            statement.executeUpdate();

        }
    }

    public void gebruikerVerwijderen(Gebruiker gebruiker, String gebruikersnaam) throws SQLException {
        for (GebruikerHasTeam ght : gebruikers) {
            if (ght.getGebruiker().getGebruikersNaam().equals(gebruikersnaam)) {
                try (Connection connection = DatabaseUtil.getConnection()) {
                    String query = "DELETE FROM gebruiker_has_team WHERE gebruiker_idGebruiker = ? AND team_idteam = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, gebruiker.getIdGebruiker());
                    statement.setInt(2, this.idTeam);
                    statement.executeUpdate();
                }
                ght.getGebruiker().removeTeam(ght);
                gebruikers.remove(ght);
            }
        }
    }


    public ScrumItem zoek(String zoekterm) {
        for (ScrumItem scrumItem : scrumItems) {
            if (scrumItem.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                return scrumItem;
            }
        }
        return null;
    }

    public ArrayList<GebruikerHasTeam> getGebruikers() {
        return gebruikers;
    }

    public void setGebruikers(ArrayList<GebruikerHasTeam> gebruikers) {
        this.gebruikers = gebruikers;
    }

    public void voegTeamToe(String teamNaam) throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO team (teamNaam) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, teamNaam);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.idTeam = generatedKeys.getInt(1);
                    System.out.println("Team: " + teamNaam + " is toegevoegd.");
                }
            }
        }
    }
}
