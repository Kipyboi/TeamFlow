package Domein;

import Utils.DatabaseUtil;
import Utils.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Gebruiker {
    private int idGebruiker;
    private String gebruikersNaam;
    private ArrayList<GebruikerHasTeam> teams;
    private ArrayList<GebruikerHasScrumItem> scrumItems;
    private ArrayList<Permissie> permissies;

    public Gebruiker(int idGebruiker, String gebruikersNaam) {
        this.idGebruiker = idGebruiker;
        this.gebruikersNaam = gebruikersNaam;
        this.teams = new ArrayList<>();
        this.scrumItems = new ArrayList<>();
    }

    public void fillScrumItems() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM Epic " +
                    "JOIN Epic_has_gebruiker ON Epic.idEpic = Epic_has_gebruiker.Epic_idEpic " +
                    "WHERE gebruiker_idGebruiker = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("Epic.idEpic");
                String naam = resultSet.getString("Epic.EpicNaam");
                String beschrijving = resultSet.getString("Epic.EpicBeschrijving");

                Epic epic = new Epic(id, naam, beschrijving);
                GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(this, epic);
                scrumItems.add(ghsi);
            }
        }
            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "SELECT * FROM Userstory " +
                        "JOIN gebruiker_has_Userstory ON Userstory.idUserstory = gebruiker_has_Userstory.Userstory_idUserstory " +
                        "WHERE gebruiker_idGebruiker = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("Userstory.idUserstory");
                    int epicId = resultSet.getInt("Userstory.Epic_idEpic");
                    String naam = resultSet.getString("Userstory.UserStoryNaam");
                    String beschrijving = resultSet.getString("Userstory.UserStoryBeschrijving");

                    UserStory us = new UserStory(id, epicId, naam, beschrijving);
                    GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(this, us);
                    scrumItems.add(ghsi);
                }
            }
            try (Connection connection = DatabaseUtil.getConnection()) {
                String query = "SELECT * FROM Taken " +
                        "JOIN gebruiker_has_Taken ON Taken.idTaken = gebruiker_has_Taken.Taken_idTaken" +
                        " WHERE gebruiker_idGebruiker = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, Session.getActiveGebruiker().getIdGebruiker());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int id = resultSet.getInt("Taken.idTaken");
                    int usId = resultSet.getInt("Userstory_idUserstory");
                    String naam = resultSet.getString("Taken.TaakNaam");
                    String beschrijving = resultSet.getString("Taken.TaakBeschrijving");

                    Taken taak = new Taken(id, usId, naam, beschrijving);
                    GebruikerHasScrumItem ghsi = new GebruikerHasScrumItem(this, taak);
                    scrumItems.add(ghsi);
                }
            }

        }


    public String getGebruikersNaam() {
        return gebruikersNaam;
    }

    public int getIdGebruiker() {
        return idGebruiker;
    }
    public void addTeam(GebruikerHasTeam ght) {
        teams.add(ght);
    }

    public void removeTeam(GebruikerHasTeam ght) {
        teams.remove(ght);
    }

    public void addScrumItem(GebruikerHasScrumItem ghsi) {
        scrumItems.add(ghsi);
    }

    public ArrayList<GebruikerHasScrumItem> getScrumItems() {
        return scrumItems;
    }

    public ArrayList<GebruikerHasTeam> getTeams() {
        return teams;
    }
}
