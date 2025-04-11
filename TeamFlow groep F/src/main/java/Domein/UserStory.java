package Domein;

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
}
