package Domein;

import java.util.ArrayList;

public class UserStory extends ScrumItem {
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
    public ScrumItem zoek() {

    }
}
