package Domein;

import Utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserStory extends ScrumItem  implements IZoek {
    private int idUserStory;
    private int Epic_idEpic;
    private ArrayList<Taken> taken;

    public UserStory(int idUserStory, int Epic_IdEpic, String scrumItemNaam) {
        super(scrumItemNaam);
        this.idUserStory = idUserStory;
        this.Epic_idEpic = Epic_IdEpic;
    }
    public UserStory(String scrumItemNaam) {
        super(scrumItemNaam);
    }

    @Override
    public ScrumItem zoek(String zoekterm) {
        for (Taken t : taken) {
            if (t.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                return t;
            }
        }
        return null;

    }
    public void gebruikerToewijzen (Gebruiker gebruiker) throws SQLException {
        GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(gebruiker, this);
        gebruiker.addScrumItem(ghsi);
        gebruikers.add(ghsi);
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Epic_has_gebruiker (gebruiker_idGebruiker, UserStory_idUserStory) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, gebruiker.getIdGebruiker());
            statement.setInt(2, this.idUserStory);
            statement.executeUpdate();
        }
    }
}
