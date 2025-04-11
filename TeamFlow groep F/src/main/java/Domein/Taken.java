package Domein;

public class Taken extends ScrumItem{
    private int UserStory_idUserStory;
    private int idTaken;

    public Taken (int UserStory_idUserStory, int IdTaken, String scrumItemNaam) {
        super(scrumItemNaam);
        this.UserStory_idUserStory = UserStory_idUserStory;
        this.idTaken = IdTaken;
    }
    public Taken (String scrumItemNaam ){
        super(scrumItemNaam);
    }


}
