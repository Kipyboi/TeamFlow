package Domein;


import java.sql.Timestamp;

public class Bericht {
    private int idBericht;
    private Timestamp tijdStamp;
    private String bericht;
    private int idGebruiker;
    private ScrumItem gekoppeldItem;

    public Bericht(int idBericht, Timestamp tijdStamp, String bericht, int idGebruiker, ScrumItem gekoppeldItem) {
        this.idBericht = idBericht;
        this.tijdStamp = tijdStamp;
        this.bericht = bericht;
        this.idGebruiker = idGebruiker;
        this.gekoppeldItem = gekoppeldItem;
    }
    public Bericht(int idBericht, Timestamp tijdStamp, String bericht, int idGebruiker) {
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
        return "idGebruiker: [" + idGebruiker + "] " + (gekoppeldItem == null ? "Geen koppeling" : gekoppeldItem.getScrumItemNaam()) + " \nidBericht: [" + idBericht + "]" + " bericht: " + bericht + " \ntijdStamp: " + tijdStamp + "";
    }
}
