package Domein;


import java.sql.Timestamp;

public class Bericht {
    private int idBericht;
    private Timestamp tijdStamp;
    private String bericht;
    private int idGebruiker;

    public Bericht(int idBericht, Timestamp tijdStamp, String bericht, int idGebruiker) {
        this.idBericht = idBericht;
        this.tijdStamp = tijdStamp;
        this.bericht = bericht;
        this.idGebruiker = idGebruiker;
    }



}
