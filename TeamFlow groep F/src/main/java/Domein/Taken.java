package Domein;

import Utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Taken extends ScrumItem{
    private int UserStory_idUserStory;
    private int idTaken;

    public Taken (int UserStory_idUserStory, int IdTaken, String scrumItemNaam, String beschrijving) {
        super(scrumItemNaam, beschrijving);
        this.UserStory_idUserStory = UserStory_idUserStory;
        this.idTaken = IdTaken;
    }
    public Taken (String scrumItemNaam, String beschrijving ){
        super(scrumItemNaam, beschrijving);
    }

    public void gebruikerToewijzen (Gebruiker gebruiker) throws SQLException {
        GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(gebruiker, this);
        gebruiker.addScrumItem(ghsi);
        gebruikers.add(ghsi);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Epic_has_gebruiker (gebruiker_idGebruiker, Taken_idTaken) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gebruiker.getIdGebruiker());
            statement.setInt(2, this.idTaken);
            statement.executeUpdate();
        }
        System.out.println("De taak is succesvol aan u toegewezen.");
    }


}
