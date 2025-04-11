package Domein;

import Utils.DatabaseUtil;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Team implements IZoek {
    private int idTeam;
    private String teamNaam;
    private ArrayList<GebruikerHasTeam> gebruikers;
    private ArrayList<ScrumItem> scrumItems;

    public Team (int idTeam, String TeamNaam) {
        this.idTeam = idTeam;
        this.teamNaam = TeamNaam;
        gebruikers = new ArrayList<>();
        scrumItems = new ArrayList<>();
    }
    public void gebruikerToevoegen(Gebruiker gebruiker) throws SQLException {
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



    public ScrumItem zoek () {

    }

    public ArrayList<GebruikerHasTeam> getGebruikers() {
        return gebruikers;
    }

    public void setGebruikers(ArrayList<GebruikerHasTeam> gebruikers) {
        this.gebruikers = gebruikers;
    }
}
