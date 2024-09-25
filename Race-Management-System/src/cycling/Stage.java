package cycling;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

//The Grace of our God be with you. Amen.
public class Stage implements Serializable {

    private ArrayList<Checkpoint> checkpoints = new ArrayList<>();
    private ArrayList<Integer> ridersInStage = new ArrayList<>();

    private HashMap<Integer, LocalTime[]> results =new HashMap<>();
    private double length;

    private int id;

    private static int StageIDCounter = 0;
    private String stageName;
    private String description;

    private LocalDateTime startTime;
    private StageType type;
    private StageState state;

    public Stage(){

    }

    public Stage(String stageName, String description, double length, LocalDateTime startTime, StageType type){
        this.length = length;
        this.stageName = stageName;
        this.description = description;
        this.startTime = startTime;
        this.type = type;
        this.id =StageIDCounter++;
    }

    public double getStageLength(){
        return length;
    }

    public int getID(){
        return id;
    }

    public StageType getType(){
        return type;
    }
    public String getName(){
        return stageName;
    }

    public StageState getState(){
        return state;
    }

    public HashMap<Integer, LocalTime[]> getResults(){
        return results;
    }

    public void removeCheckpoint(int checkpointId){
        Checkpoint checkpointToRemove = null;
        for(Checkpoint checkpoint: checkpoints){
            if(checkpoint.getID() == checkpointId){
                checkpointToRemove = checkpoint;
            }
        }
        checkpoints.remove(checkpointToRemove);
    }
    public void setState(){
        this.state = StageState.WAITING_FOR_RESULTS;
    }
    public void addCategorisedClimbToStage(Checkpoint checkpoint){
        checkpoints.add(checkpoint);
    }

    public void addIntermediateSprintToStage(Checkpoint checkpoint){
        checkpoints.add(checkpoint);
    }

    public ArrayList<Checkpoint> getStageCheckpoints(){
        return checkpoints;
    }

    public void registerRiderResultsInStage(int riderID, LocalTime... Checkpoints){
        this.results.put(riderID, Checkpoints);
        this.ridersInStage.add(riderID);
        //at this level everything works
    }

    public LocalTime getRiderAdjustedElapsedTimeInStages(int riderId){
        LocalTime[] allEndTimes = getRiderCheckpointTimes(checkpoints.size()-1);
        LocalTime[] sortedEndTimes = sortRiderResults(allEndTimes);

        LocalTime[] riderTimes = results.get(riderId);
        LocalTime ridersEndTime = riderTimes[riderTimes.length-1];
        LocalTime adjustedElapsedTime = null;



        for (int i = sortedEndTimes.length-1; i >= 0; i--) {
            Duration duration = Duration.between( sortedEndTimes[i], ridersEndTime);
            //   Duration duration = Duration.between( ridersEndTime, sortedEndTimes[i]);
            if (duration.getSeconds() < 1) {
                adjustedElapsedTime = sortedEndTimes[i];
            } else {
                break;
            }
        }


        return adjustedElapsedTime;
    }
    public LocalTime[] getRiderResultsInStage(int riderID){
        return results.get(riderID);
    }

    public void deleteRiderResultsInStage(int riderID){
        results.remove(riderID);
    }

    public HashMap<LocalTime, ArrayList<Integer>> reverseResultsHashMap(int checkpointIndex){
        //Set<Integer> riders = results.keySet();
        HashMap<LocalTime, ArrayList<Integer>> reversedResults = new HashMap<>();

        for(Integer rider: ridersInStage){
            LocalTime[] times = results.get(rider);
            LocalTime finishTime = times[checkpointIndex];
            if (!reversedResults.containsKey(finishTime)) {
                ArrayList<Integer> value = new ArrayList<>();
                value.add(rider);
                reversedResults.put(finishTime, value);
            }
            else{
                ArrayList<Integer> value = reversedResults.get(finishTime);
                value.add(rider);
                reversedResults.replace(finishTime, value);
            }
        }

        Set<LocalTime> times = reversedResults.keySet();

        return reversedResults;
    }

    public LocalTime[] sortRiderResults(LocalTime[] finishTimes){

        int n = results.size();
        for(int i = 1; i < n; i++){
            LocalTime key = finishTimes[i];
            int j = i-1;

            while(j>=0 && finishTimes[j].isAfter(key)){
                finishTimes[j+1] = finishTimes[j];
                j = j-1;
            }
            finishTimes[j+1] = key;
        }

        return finishTimes;
    }

    public int[] getRidersInStage(){
        //Set<Integer> r = results.keySet();

        int[] riders = new int[ridersInStage.size()];
        int i = 0;
        for(Object rider: ridersInStage){
            riders[i] = (int)rider;
            i++;
        }
        return riders;
    }

    public int[] getRidersRankInStage(){
        int[] rankedRiders = new int[results.size()];
        HashMap<LocalTime, ArrayList<Integer>> reversedResults = reverseResultsHashMap(checkpoints.size()-1);
        LocalTime[] times = sortRiderResults(getRiderCheckpointTimes(checkpoints.size()-1));

        int i =0;
        for(LocalTime time: times){
            if(reversedResults.get(time).size() == 1){
                rankedRiders[i] = reversedResults.get(time).get(0);
                i++;
            }
            else{
                for(int j = 0; j<reversedResults.get(time).size(); j++){
                    rankedRiders[i]= reversedResults.get(time).get(j);
                    i++;
                }

            }
        }

        return rankedRiders;

    }

    public LocalTime[] getRiderCheckpointTimes(int checkpointIndex){
        LocalTime[] finishTimes = new LocalTime[results.size()];
        int i = 0;
        for(int rider: ridersInStage){
            LocalTime[] result = results.get(rider);
            finishTimes[i] = result[checkpointIndex];
            i++;
        }

        return finishTimes;
    }

    //overloaded method
    public int[] getRidersRankInStage(int checkpointIndex){
        int[] rankedRiders = new int[results.size()];
        HashMap<LocalTime, ArrayList<Integer>> reversedResults = reverseResultsHashMap(checkpointIndex);
        LocalTime[] times = sortRiderResults(getRiderCheckpointTimes(checkpoints.size()-1));

        int i =0;
        for(LocalTime time: times){
            if(reversedResults.get(time).size() == 1){
                rankedRiders[i] = reversedResults.get(time).get(0);
                //System.out.println(reversedResults.get(time).get(0));
                i++;
            }
            else{
                for(int j = 0; j<reversedResults.get(time).size(); j++){
                    rankedRiders[i]= reversedResults.get(time).get(j);
                    i++;
                }

            }
        }

        return rankedRiders;

    }

    public int[] getRiderPointsInStage(){
        HashMap<Integer,Integer> riderPoints = new HashMap<>();
        int[] rankedRiders = getRidersRankInStage();
        int[] points = new int[rankedRiders.length];

        for(int checkpointIndex = 0; checkpointIndex<checkpoints.size(); checkpointIndex++){
            CheckpointType type = checkpoints.get(checkpointIndex).getCheckPointType();
            int[] ridersRanked = getRidersRankInStage(checkpointIndex);

            switch (type){
                case C1:
                    int[] points1 = {10,8,6,4,2,1};
                    riderPoints = setRiderPoints(points1, ridersRanked, riderPoints);
                    break;
                case C2:
                    int[] points2 = {5,3,2,1};
                    riderPoints = setRiderPoints(points2, ridersRanked, riderPoints);
                    break;
                case C3:
                    int[] points3 = {2,1};
                    riderPoints = setRiderPoints(points3, ridersRanked, riderPoints);
                    break;
                case C4:
                    int[] points4 = {1};
                    riderPoints = setRiderPoints(points4, ridersRanked, riderPoints);
                    break;
                case HC:
                    int[] pointsHC = {20,15,12,10,8,6,4,2};
                    riderPoints = setRiderPoints(pointsHC, ridersRanked, riderPoints);
                    break;
                case SPRINT:
                    int[] pointsSPRINT = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
                    riderPoints = setRiderPoints(pointsSPRINT, ridersRanked, riderPoints);
                    break;
            }

            for(int i =0;i<rankedRiders.length;i++){
                points[i] = riderPoints.get(rankedRiders[i]);
            }
        }

        return points;
    }

    public HashMap<Integer, Integer> setRiderPoints(int[] points, int[] ridersRanked, HashMap<Integer,Integer> riderPoints ){
        //riderPoints = new HashMap<>();
        for (int i = 0; i< points.length; i++){
            if(!riderPoints.containsKey(ridersRanked[i])){
                riderPoints.put(ridersRanked[i], points[i]);
            }
            else{
                int adjustedPoints = points[i] + riderPoints.get(ridersRanked[i]);
                riderPoints.replace(ridersRanked[i], adjustedPoints);
            }
        }

        return riderPoints;
    }

    public int[] getRiderMountainPointsInStage(){
        HashMap<Integer,Integer> riderPoints = new HashMap<>();
        int[] rankedRiders = getRidersRankInStage();
        int[] points = new int[rankedRiders.length];

        StageType stageType = type;
        switch(stageType){
            case MEDIUM_MOUNTAIN:
                int[] pointsMM = {30,25,22,19,17,15,13,11,9,7,6,5,4,3,2};
                riderPoints = setRiderPoints(pointsMM,rankedRiders,riderPoints);
                break;
            case HIGH_MOUNTAIN:
                int[] pointsHM = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
                riderPoints = setRiderPoints(pointsHM,rankedRiders,riderPoints);
                break;
            case FLAT:
                int[] pointsF = {50,30,20,18,16,14,12,10,8,7,6,5,4,3,2};
                riderPoints = setRiderPoints(pointsF,rankedRiders,riderPoints);
                break;
            case TT:
                int[] pointsTT = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
                riderPoints = setRiderPoints(pointsTT, rankedRiders, riderPoints);
                break;
        }
        for(int i = 0; i<rankedRiders.length; i++){
            points[i] = riderPoints.get(rankedRiders[i]);
        }

        return points;
    }

    public static void resetCounter(){
        StageIDCounter = 0;
    }
    public LocalTime[] getRankedAdjustedElapsedTimesInStages(){
        LocalTime[] times = new LocalTime[ridersInStage.size()];

        int i = 0;
        for(int rider: ridersInStage){
            times[i] = getRiderAdjustedElapsedTimeInStages(rider);
            i++;
        }

        return sortRiderResults(times);
    }
    // @Override
	// public double getStageLength(int stageId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public void removeStageById(int stageId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub

	// }

	// @Override
	// public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
	// 		Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
	// 		InvalidStageTypeException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
	// 		InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
	// 	// TODO Auto-generated method stub
	// 	return 0;
	// }

	// @Override
	// public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
	// 	// TODO Auto-generated method stub

	// }

	// @Override
	// public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
	// 	// TODO Auto-generated method stub

	// }

	// @Override
	// public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {
	// 	// TODO Auto-generated method stub
	// 	return null;
	// }
}
