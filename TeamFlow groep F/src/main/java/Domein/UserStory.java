package Domein;

import Utils.DatabaseUtil;
import Utils.Session;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class UserStory extends ScrumItem  implements IZoek {
    private int idUserStory;
    private int Epic_idEpic;
    private ArrayList<Taken> taken;
    private ArrayList<GebruikerHasScrumItem> gebruikers = new ArrayList<>();

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
    public void BerichtAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder het bericht dat je wilt posten (enter om te versturen): ");
        String berichtTekst = scanner.nextLine();

        if (berichtTekst.isBlank()) {
            System.out.println("Bericht mag niet leeg zijn!");
            return;
        }

        LocalDate currentDate = LocalDate.now();
        Date sqlDate = Date.valueOf(currentDate);

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Bericht_UserStory " +
                    "(tijdStamp, bericht, gebruiker_idGebruiker, user_story_idUserStory)" +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, sqlDate);
            statement.setString(2, berichtTekst);
            statement.setInt(3, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(4, idUserStory);
            statement.executeUpdate();
        }

        Bericht bericht = new Bericht(-1, sqlDate, berichtTekst, Session.getActiveGebruiker().getIdGebruiker(), null);
        System.out.println("Bericht aangemaakt voor de user story!");
    }
}
