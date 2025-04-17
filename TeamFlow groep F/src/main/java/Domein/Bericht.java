package Domein;


import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Bericht {
    private int idBericht;
    private Date tijdStamp;
    private String bericht;
    private int idGebruiker;
    private ScrumItem gekoppeldItem;

    public Bericht(int idBericht, Date tijdStamp, String bericht, int idGebruiker, ScrumItem gekoppeldItem) {
        this.idBericht = idBericht;
        this.tijdStamp = tijdStamp;
        this.bericht = bericht;
        this.idGebruiker = idGebruiker;
        this.gekoppeldItem = gekoppeldItem;
    }
    public Bericht(int idBericht, Date tijdStamp, String bericht, int idGebruiker) {
        this(idBericht, tijdStamp, bericht, idGebruiker, null);
    }

    public ScrumItem getGekoppeldItem() {
        return gekoppeldItem;
    }
    public void setGekoppeldItem(ScrumItem gekoppeldItem) {
        this.gekoppeldItem = gekoppeldItem;
    }

    @Override
    public String toString() {
        String gebruikersnaam = "Onbekend";
        try {
            GebruikerService gebruikerService = new GebruikerService();
            ArrayList<Gebruiker> gebruikers = gebruikerService.getGebruikers();

            for (Gebruiker gebruiker : gebruikers) {
                if (this.idGebruiker == gebruiker.getIdGebruiker()) {
                    gebruikersnaam = gebruiker.getGebruikersNaam();
                    break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tijdStamp.toString() + "\n" + gebruikersnaam + "\n" + bericht;
    }

}
