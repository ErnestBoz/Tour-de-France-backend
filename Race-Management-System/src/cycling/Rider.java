package cycling;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
public class Rider implements Serializable {
    private String name;
    private int riderId;
    private int teamId;
    private int score;
    private static int RiderIDCounter = 0;
    private int yearOfBirth;




    public Rider(int teamId, String name, int yearOfBirth){
        this.teamId = teamId;
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        this.riderId = ++RiderIDCounter;
    }


    public int getTeamId(){
        return this.teamId;
    }


    public String getName(){
        return this.name;
    }

    public int getRiderId(){
        return this.riderId;
    }

    public void setId(int id){
        this.riderId = id;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void incrementScore(int addedScore){
        this.score += addedScore;
    }

    public static void resetCounter(){
        RiderIDCounter = 0;
    }

    // @Override
	// public int createRider(int teamID, String name, int yearOfBirth)
	// 		throws IDNotRecognisedException, IllegalArgumentException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public void removeRider(int riderId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub
    // also removes the results of the rider
    

	// }

    /*@Override
    public int createRider(int teamID, String name, int yearOfBirth)
            throws IDNotRecognisedException, IllegalArgumentException {
		*//*Rider rider = new Rider(name, teamID, yearOfBirth);
		this.MyRiders.add(rider);
		rider.yearOfBirth = yearOfBirth;*//*

        return 0;
    }*/
}
