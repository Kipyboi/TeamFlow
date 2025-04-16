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

public class Taken extends ScrumItem{
    private int UserStory_idUserStory;
    private int idTaken;
    ArrayList<GebruikerHasScrumItem> gebruikers = new ArrayList<>();

    public Taken (int UserStory_idUserStory, int IdTaken, String scrumItemNaam) {
        super(scrumItemNaam);
        this.UserStory_idUserStory = UserStory_idUserStory;
        this.idTaken = IdTaken;
    }
    public Taken (String scrumItemNaam ){
        super(scrumItemNaam);
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
            String query = "INSERT INTO Bericht_taken " +
                    "(tijdStamp, bericht, gebruiker_idGebruiker, taken_idTaken)" +
                    " VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, sqlDate);
            statement.setString(2, berichtTekst);
            statement.setInt(3, Session.getActiveGebruiker().getIdGebruiker());
            statement.setInt(4, idTaken);
            statement.executeUpdate();
        }

        Bericht bericht = new Bericht(-1, sqlDate, berichtTekst, Session.getActiveGebruiker().getIdGebruiker(), null);
        System.out.println("Bericht aangemaakt voor de taak!");
    }


}
