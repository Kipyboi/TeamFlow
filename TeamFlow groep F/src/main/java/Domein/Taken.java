package Domein;

public class Taken extends ScrumItem{
    private int UserStory_IdUserStory;
    private int IdTaken;

    public Taken (int UserStory_IdUserStory, int IdTaken, String scrumItemNaam) {
        super(scrumItemNaam);
        this.UserStory_IdUserStory = UserStory_IdUserStory;
        this.IdTaken = IdTaken;
    }
    public Taken (String scrumItemNaam ){
        super(scrumItemNaam);
    }

    @Override
    public ScrumItem zoek() {

    }
}
