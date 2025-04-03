package Domein;

import java.util.ArrayList;

public abstract class ScrumItem implements IZoek {
    protected String scrumItemNaam;
    protected int idScrumItem;
    protected int status;
    protected ArrayList<GebruikerHasScrumItem> gebruikers;



}
