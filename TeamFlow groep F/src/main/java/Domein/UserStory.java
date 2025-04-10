package Domein;

import java.util.ArrayList;

public class UserStory extends ScrumItem {
    private int IdUserStory;
    private int Epic_IdEpic;
    private ArrayList<Taken> taken;

    public UserStory(int idUserStory, int Epic_IdEpic, String scrumItemNaam) {
        super(scrumItemNaam);
        this.IdUserStory = idUserStory;
        this.Epic_IdEpic = Epic_IdEpic;
    }
    public UserStory(String scrumItemNaam) {
        super(scrumItemNaam);
    }

    @Override
    public ScrumItem zoek() {

    }
}
