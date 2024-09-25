package cycling;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class Race implements Serializable {
    private double length;
    private String name;
    private Date date;

    private String description;

    private static int RaceIDCounter = 0;

    private int id;

    private ArrayList<Stage> stages = new ArrayList<>();
    private ArrayList<Integer> ridersInRace = new ArrayList<>();

    private HashMap<Integer, LocalTime> riderTimes = new HashMap<>();


    public Race(){
        this.id = RaceIDCounter++;
    }
    public Race(String name, String description){
        this.name = name;
        this.description = description;
        this.id = RaceIDCounter++;
    }

    // getter method that returns the id
    public int getId(){
        return id;
    }

    // getter method that returns the description
    public String getDescription(){
        return description;
    }

    public int getNumberOfStages(){
        return stages.size();
    }

    public String getName(){
        return name;
    }

    public double getLength(){
        int length = 0;
        for(Stage s: stages){
            length+= s.getStageLength();
        }
        return length;
    }
    public void addStageToRace(Stage stage){
        stages.add(stage);
        System.out.print(stages);
    }

    public ArrayList<Stage> getRaceStages(){
        return stages;
    }

    public void removeStageById(int stageID){
        Stage stageToRemove = null;
        for(Stage s: stages){
            if(s.getID() == stageID)
                stageToRemove = s;
        }

        stages.remove(stageToRemove);
    }

    public LocalTime[] getGeneralClassificationTimesInRace(){
        LocalTime[] times = new LocalTime[riderTimes.size()];

        for(Stage stage: stages){
            int[] riders = stage.getRidersInStage();
            for(int rider: riders){
                ridersInRace.add(rider);
                LocalTime ridersAdjustedElapsedTime = stage.getRiderAdjustedElapsedTimeInStages(rider);
                if(!riderTimes.containsKey(rider)){
                    riderTimes.put(rider, ridersAdjustedElapsedTime);
                }
                else{
                    int hoursSum = 0;
                    int minutesSum = 0;
                    int secondsSum = ridersAdjustedElapsedTime.getSecond() + riderTimes.get(rider).getSecond();
                    if(secondsSum>60){
                        minutesSum = 1;
                        secondsSum = secondsSum - 60;
                    }
                    minutesSum = minutesSum+ ridersAdjustedElapsedTime.getMinute() + riderTimes.get(rider).getMinute();
                    if(minutesSum>60){
                        hoursSum = 1;
                        minutesSum = minutesSum -60;
                    }
                    hoursSum = hoursSum + ridersAdjustedElapsedTime.getHour() + riderTimes.get(rider).getHour();

                    LocalTime sum = LocalTime.of(hoursSum, minutesSum, secondsSum);
                    riderTimes.replace(rider, sum);

                }
            }

        }
        int i =0;
        for(Integer rider: ridersInRace){
            times[i] = riderTimes.get(rider);
            i++;
        }

        return sortRiderResults(times, riderTimes.size());
    }

    public LocalTime[] sortRiderResults(LocalTime[] elapsedTime, int n){

        //int n = results.size();
        for(int i = 1; i < n; i++){
            LocalTime key = elapsedTime[i];
            int j = i-1;

            while(j>=0 && elapsedTime[j].isAfter(key)){
                elapsedTime[j+1] = elapsedTime[j];
                j = j-1;
            }
            elapsedTime[j+1] = key;
        }

        return elapsedTime;
    }

    public int[] getRidersPointsInRace(){
        HashMap<Integer, Integer> riderPoints = new HashMap<>();
        int[] rPoints = new int[ridersInRace.size()];

        for(Stage stage: stages){
            int[] riders = stage.getRidersInStage();
            int[] points = stage.getRiderPointsInStage();
            int[] rankings = stage.getRidersRankInStage();
            int i = 0;
            for(int rider: rankings){
                if(!riderPoints.containsKey(rider)){
                    riderPoints.put(rider, points[i]);
                }
                else{
                    int sum = points[i] + riderPoints.get(rider);
                    riderPoints.replace(rider, sum);
                }
                i++;
            }
        }

        int i = 0;
        for(int rider: getRidersGeneralClassificationRank()){
            rPoints[i] = riderPoints.get(rider);
            i++;
        }

        return rPoints;
    }

    public int[] getRidersMountainPointsInRace(){
        HashMap<Integer, Integer> riderMountainPoints = new HashMap<>();
        int[] rMountainPoints = new int[ridersInRace.size()];

        for(Stage stage: stages){
            int[] mountainPoints = stage.getRiderMountainPointsInStage();
            int[] rankedRiders = stage.getRidersRankInStage();
            int i = 0;
            for(int rider: rankedRiders){
                if(!riderMountainPoints.containsKey(rider))
                    riderMountainPoints.put(rider, mountainPoints[i]);
                else{
                    int sum = mountainPoints[i] + riderMountainPoints.get(rider);
                    riderMountainPoints.replace(rider, sum);
                }
                i++;
            }
        }

        int i = 0;
        for(int rider: getRidersGeneralClassificationRank()){
            rMountainPoints[i] = riderMountainPoints.get(rider);
            i++; //maybe remove this shit
        }


        return rMountainPoints;
    }

    public int[] getRidersGeneralClassificationRank(){
        HashMap<LocalTime, ArrayList<Integer>> reversedRiderTimes = new HashMap<>();
        int[] rankedRiders = new int[ridersInRace.size()];

        for (int rider:ridersInRace){
            LocalTime adjustedElapsedTime = riderTimes.get(rider);
            if(!reversedRiderTimes.containsKey(adjustedElapsedTime)){
                ArrayList<Integer> riders = new ArrayList<>();
                riders.add(rider);
                reversedRiderTimes.put(adjustedElapsedTime, riders);
            }
            else{
                ArrayList<Integer> riders = reversedRiderTimes.get(adjustedElapsedTime);
                riders.add(rider);
                reversedRiderTimes.replace(adjustedElapsedTime, riders);
            }
        }
        LocalTime[] adjustedElapsedTimes = getGeneralClassificationTimesInRace();
        int i = 0;
        for(LocalTime time:adjustedElapsedTimes){
            if(reversedRiderTimes.get(time).size() == 1){
                rankedRiders[i] = reversedRiderTimes.get(time).get(0);
                i++;
            }
            else{
                for(int j = 0; j<reversedRiderTimes.get(time).size(); j++){
                    rankedRiders[i] = reversedRiderTimes.get(time).get(j);
                    i++;
                }
            }

        }
        return rankedRiders;
    }

    public int[] getRidersPointClassificationRank(){
        HashMap<Integer, ArrayList<Integer>> pointRiderDictionary = new HashMap<>();
        int[] correspondingIds = getRidersGeneralClassificationRank();
        int[] points = getRidersPointsInRace();

        int i = 0;
        for (int point: points){
            if(!pointRiderDictionary.containsKey(point)){
                ArrayList<Integer> riders = new ArrayList<>();
                riders.add(correspondingIds[i]);
                pointRiderDictionary.put(point, riders);
            }
            else{
                ArrayList<Integer> riders = pointRiderDictionary.get(point);
                riders.add(correspondingIds[i]);
                pointRiderDictionary.replace(point, riders);
            }
            i++;
        }
        int[] sortedPoints = sortPoints(points);
        int[] sortedIds = new int[ridersInRace.size()];

        int x = 0;
        for(int point: sortedPoints){
            if(pointRiderDictionary.get(point).size() == 1){
                sortedIds[x] = pointRiderDictionary.get(point).get(0);
                x++;
            }
            else {
                for(int y = 0; y< pointRiderDictionary.get(point).size(); y++){
                    sortedIds[x] = pointRiderDictionary.get(point).get(y);
                    x++;
                }
            }
        }

        return sortedIds;
    }

    public int[] getRidersMountainPointClassificationRank(){
        HashMap<Integer, ArrayList<Integer>> pointRiderDictionary = new HashMap<>();
        int[] correspondingIds = getRidersGeneralClassificationRank();
        int[] points = getRidersMountainPointsInRace();

        int i = 0;
        for (int point: points){
            if(!pointRiderDictionary.containsKey(point)){
                ArrayList<Integer> riders = new ArrayList<>();
                riders.add(correspondingIds[i]);
                pointRiderDictionary.put(point, riders);
            }
            else{
                ArrayList<Integer> riders = pointRiderDictionary.get(point);
                riders.add(correspondingIds[i]);
                pointRiderDictionary.replace(point, riders);
            }
            i++;
        }
        int[] sortedPoints = sortPoints(points);
        int[] sortedIds = new int[ridersInRace.size()];

        int x = 0;
        for(int point: sortedPoints){
            if(pointRiderDictionary.get(point).size() == 1){
                sortedIds[x] = pointRiderDictionary.get(point).get(0);
                x++;
            }
            else {
                for(int y = 0; y< pointRiderDictionary.get(point).size(); y++){
                    sortedIds[x] = pointRiderDictionary.get(point).get(y);
                    x++;
                }
            }
        }

        return sortedIds;
    }

    public int[] sortPoints(int[] points){
        int[] sortedPoints = new int[points.length];

        int n = points.length;
        for (int i = 1; i < n; ++i) {
            int key = points[i];
            int j = i - 1;

            /* Move elements of arr[0..i-1], that are
               greater than key, to one position ahead
               of their current position */
            while (j >= 0 && points[j] > key) {
                points[j + 1] = points[j];
                j = j - 1;
            }
            points[j + 1] = key;
        }
        int j = 0;
        for(int i = points.length-1; i>=0; i--){
            sortedPoints[j] = points[i];
        }
        return sortedPoints;
    }


    public static void resetCounter(){
        RaceIDCounter = 0;
    }
	// @Override
	// public int[] getRaceIds() {
	// 	// TODO Auto-generated method stub
	// 	return new int[] {};
	// }

	// @Override
	// public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }

	// @Override
	// public void removeRaceById(int raceId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub

	// }

	// @Override
	// public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
	// 		StageType type)
	// 		throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }
}

