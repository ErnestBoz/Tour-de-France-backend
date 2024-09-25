package cycling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {

    private String name;

    private String description;
    private int teamId;
    private int score;
    private static int TeamIDCounter = 0;
    private ArrayList<Integer> ridersInTeam;

    // ridersInTeam = new ArrayList<>();

    public Team(String name, String description){
        this.name = name;
        this.description = name;
        this.teamId = ++TeamIDCounter;

    }

    public void removeTeam(int teamId){
        if (this.teamId == teamId){
            this.name = null;
            this.description = null;
        }

    }

    public int getTeamId(){
        return this.teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return teamId;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void addRider(int value) {
        ridersInTeam.add(value);
    }

    public static void resetCounter(){
        TeamIDCounter = 0;
    }

    // @Override
	// public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public void removeTeam(int teamId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub

	// }

	// @Override
	// public int[] getTeams() {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }

	// @Override
	// public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }

}
