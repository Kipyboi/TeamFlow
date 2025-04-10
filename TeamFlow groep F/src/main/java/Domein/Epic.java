package Domein;

import java.util.ArrayList;

public class Epic extends ScrumItem {
    private int idEpic;
    ArrayList<UserStory> UserStories;

    public Epic (int idEpic, String scrumItemNaam) {
        super(scrumItemNaam);
        this.idEpic = idEpic;
    }
    public Epic (String scrumItemNaam) {
        super(scrumItemNaam);
    }

    @Override
    public ScrumItem zoek() {

    }
}
