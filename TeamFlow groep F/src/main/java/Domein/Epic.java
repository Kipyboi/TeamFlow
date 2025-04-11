package Domein;

import Utils.DatabaseUtil;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Epic extends ScrumItem  implements IZoek {
    private int idEpic;
    ArrayList<UserStory> UserStories;

    public Epic (int idEpic, String scrumItemNaam) {
        super(scrumItemNaam);
        this.idEpic = idEpic;
    }
    public Epic (String scrumItemNaam) {
        super(scrumItemNaam);
    }

    @Override
    public ScrumItem zoek(String zoekterm) {
        for (UserStory us : UserStories) {
            if (us.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                return us;
            }
        }
        return null;
    }
    public void gebruikerToewijzen (Gebruiker gebruiker) throws SQLException {
        GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(gebruiker, this);
        gebruiker.addScrumItem(ghsi);
        gebruikers.add(ghsi);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Epic_has_gebruiker (gebruiker_idGebruiker, Epic_idEpic) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gebruiker.getIdGebruiker());
            statement.setInt(2, this.idEpic);
            statement.executeUpdate();
        }
    }
}
