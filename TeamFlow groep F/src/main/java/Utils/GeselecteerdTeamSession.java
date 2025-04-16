package Utils;

import Domein.Team;

public class GeselecteerdTeamSession {
    private static Team geselecteerdTeam;

    public static Team getGeselecteerdTeam() {
        return geselecteerdTeam;
    }
    public static void setGeselecteerdTeam(Team team) {
        geselecteerdTeam = team;
    }
    public static void clear () {
        geselecteerdTeam = null;
    }
}
