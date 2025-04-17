package Domein;

import Utils.DatabaseUtil;
import Utils.GeselecteerdTeamSession;
import Utils.GeselecteerdeEpicSession;
import Utils.Session;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Epic extends ScrumItem  implements IZoek, IMenu {
    private int idEpic;
    ArrayList<UserStory> UserStories;

    public Epic (int idEpic, String scrumItemNaam, String beschrijving) {
        super(scrumItemNaam, beschrijving);
        this.idEpic = idEpic;
    }
    public Epic (String scrumItemNaam, String beschrijving) {
        super(scrumItemNaam, beschrijving);
    }

    @Override
    public void zoek(Scanner scanner) {
        System.out.println("Typ hieronder de naam in van de user story die u zoekt");
        String zoekterm = scanner.nextLine();
        Epic geselecteerdeEpic = GeselecteerdeEpicSession.getGeselecteerdeEpic();
        for (UserStory us : geselecteerdeEpic.getUserStories()) {
            if (us.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                System.out.println(us.getScrumItemNaam());
                System.out.println();
            }
        }
        System.out.println("Typ de naam van de epic die u wilt bekijken.");
        String usNaam = scanner.nextLine();
        for (UserStory us : geselecteerdeEpic.getUserStories()) {
            if (us.getScrumItemNaam().equalsIgnoreCase(usNaam)) {
                us.gaNaar(scanner);
            }
        }

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
        System.out.println("De Epic is succesvol aan u toegewezen.");
    }
    @Override
    public void BerichtAanmaken (Scanner scanner) throws SQLException {
        boolean toegewezen = false;
        for (GebruikerHasScrumItem ghsi : Session.getActiveGebruiker().getScrumItems()) {
            if (ghsi.getScrumItem().getGebruikers().contains(Session.getActiveGebruiker())) {
                toegewezen = true;
            }
        }

        if (toegewezen) {
            System.out.println("Typ hieronder het bericht dat je wilt posten (enter om te versturen): ");
            String berichtTekst = scanner.nextLine();

            LocalDate currentDate = LocalDate.now();
            Date sqlDate = Date.valueOf(currentDate);

            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "INSERT INTO Bericht_Epic " +
                        "(tijdStamp, bericht, Epic_has_gebruiker_gebruiker_idGebruiker, " +
                        "Epic_has_gebruiker_Epic_idEpic)" +
                        " VALUES (?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setDate(1, sqlDate);
                statement.setString(2, berichtTekst);
                statement.setInt(3, Session.getActiveGebruiker().getIdGebruiker());
                statement.setInt(4, this.idEpic);
                statement.executeUpdate();
            }

            Bericht bericht = new Bericht(-1, sqlDate, berichtTekst, Session.getActiveGebruiker().getIdGebruiker(), this);
            System.out.println("Bericht gepost!");
            GeselecteerdeEpicSession.getGeselecteerdeEpic().gaNaar(scanner);
        }
        else {
            System.out.println("U bent niet toegewezen aan deze epic en kan er daarom geen berichten over posten.");
            GeselecteerdeEpicSession.getGeselecteerdeEpic().gaNaar(scanner);
        }
    }

    @Override
    public void UserstoryAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder de naam van de userstory die je wilt aanmaken (enter om te versturen): ");
        String userstoryNaam = scanner.nextLine();
        System.out.println("Typ hieronder de beschrijving van " + userstoryNaam +" (enter om te versturen): ");
        String userstorybeschrijving = scanner.nextLine();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO Userstory (UserStoryNaam, Epic_idEpic, UserStoryBeschrijving) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, userstoryNaam);
            statement.setInt(2, this.idEpic);
            statement.setString(3, userstorybeschrijving);

            statement.executeUpdate();
        }

        UserStory userstory = new UserStory (userstoryNaam, userstorybeschrijving);
        this.UserStories.add(userstory);

        System.out.println("Userstory: " + userstoryNaam + " toegevoegd aan epic:" + scrumItemNaam + "!");
    }

    public ArrayList<UserStory> getUserStories() {
        return UserStories;
    }

}
