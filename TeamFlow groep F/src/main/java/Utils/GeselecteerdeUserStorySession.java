package Utils;

import Domein.UserStory;

public class GeselecteerdeUserStorySession {
    private static UserStory geselecteerdeUserStory;

    public static UserStory getGeselecteerdeUserStory() {
        return geselecteerdeUserStory;
    }
    public static void setGeselecteerdeUserStory(UserStory us) {
        geselecteerdeUserStory = us;
    }
    public static void clear () {
        geselecteerdeUserStory = null;
    }
}
