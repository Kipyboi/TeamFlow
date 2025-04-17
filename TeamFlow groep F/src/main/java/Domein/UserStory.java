package Domein;

import Utils.DatabaseUtil;
import Utils.GeselecteerdeEpicSession;
import Utils.GeselecteerdeUserStorySession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

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
    public void zoek(Scanner scanner) {
        System.out.println("Typ hieronder de naam in van de taak die u zoekt");
        String zoekterm = scanner.nextLine();
        UserStory geselecteerdeUserStory = GeselecteerdeUserStorySession.getGeselecteerdeUserStory();
        for (Taken t : geselecteerdeUserStory.getTaken()) {
            if (t.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                System.out.println(t.getScrumItemNaam());
                System.out.println();
            }
        }
        System.out.println("Typ de naam van de epic die u wilt bekijken.");
        String usNaam = scanner.nextLine();
        for (Taken t : geselecteerdeUserStory.getTaken()) {
            if (t.getScrumItemNaam().equalsIgnoreCase(usNaam)) {
                t.gaNaar(scanner);
            }
        }

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
        System.out.println("De User Story is succesvol aan u toegewezen.");
    }

    @Override
    public void TaakAanmaken (Scanner scanner) throws SQLException {
        System.out.println("Typ hieronder de naam van de taak die je wilt aanmaken (enter om te versturen): ");
        String taakNaam = scanner.nextLine();
        System.out.println("Typ hieronder de beschrijving van " + taakNaam +" (enter om te versturen): ");
        String taakBeschrijving = scanner.nextLine();

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO taken (TaakNaam, Userstory_idUserstory, TaakBeschrijving) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, taakNaam);
            statement.setInt(2, this.idUserStory);
            statement.setString(3, taakBeschrijving);

            statement.executeUpdate();
        }

        Taken taak = new Taken (taakNaam, taakBeschrijving);
        this.taken.add(taak);

        System.out.println("Taak: " + taakNaam + " toegevoegd aan userstory:" + super(scrumItemNaam) + "!");
    }

    public ArrayList<Taken> getTaken() {
        return taken;
    }
}
