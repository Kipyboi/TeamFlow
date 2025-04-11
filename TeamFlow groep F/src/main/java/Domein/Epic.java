package Domein;

import java.util.ArrayList;

public class Epic extends ScrumItem  implements IZoek {
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
    public ScrumItem zoek(String zoekterm) {
        for (UserStory us : UserStories) {
            if (us.getScrumItemNaam().toLowerCase().contains(zoekterm.toLowerCase())) {
                return us;
            }
        }
        return null;
    }
}
