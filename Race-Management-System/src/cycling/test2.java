package cycling;
import java.util.Arrays;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.IOException;

public class test2 {
    public static void main(String[] args) throws IllegalNameException, InvalidNameException, IDNotRecognisedException, InvalidLengthException,InvalidLocationException, InvalidStageStateException,
    InvalidStageTypeException,DuplicatedResultException, InvalidCheckpointTimesException, IOException, NameNotRecognisedException{
        CyclingPortal CyclingPortal = new CyclingPortalImpl();
        CyclingPortal.createRace("TourFR","The famous cycling race");
        CyclingPortal.createRace("TourEU", "The famous cycling race in EU");
        CyclingPortal.createRace("bro", "hey");
        


        
        CyclingPortal.removeRaceById(2);

        int[] raceIds = CyclingPortal.getRaceIds();
        System.out.println(raceIds);
        if (raceIds.length > 0) {
            System.out.println("Race IDs:");
        
            // Iterate through the array and print each ID
            for (int raceId : raceIds) {
                System.out.println(raceId);
            }
        } else {
            System.out.println("No races found.");
        }


        // String racedetail2 = CyclingPortal.viewRaceDetails(1);
        // System.out.println(racedetail2);


        LocalDateTime startTime = LocalDateTime.of(2022, 5, 15, 14, 30);
        CyclingPortal.addStageToRace(0,"Theboulder1","An epic climb against boulders",6.0,startTime,StageType.HIGH_MOUNTAIN);
        CyclingPortal.addStageToRace(0,"Theboulder2","An epic climb against boulders 2",7.0,startTime,StageType.HIGH_MOUNTAIN);
        CyclingPortal.addStageToRace(0, "Theboulder3", "bruh", 5, startTime, StageType.HIGH_MOUNTAIN);
       


        String racedetail1 = CyclingPortal.viewRaceDetails(0);
        System.out.println(racedetail1);

        CyclingPortal.removeStageById(1);
        int nostage = CyclingPortal.getNumberOfStages(0);
        System.out.println(nostage);

        System.out.println(" ");

        int[] getstages = CyclingPortal.getRaceStages(0);
        for ( int stages: getstages){
            System.out.println(stages);
        }

        double stageLength = CyclingPortal.getStageLength(2);
        System.out.println(stageLength);

        CyclingPortal.concludeStagePreparation(0);

        System.out.println(" ");

        CyclingPortal.createTeam("StrawHats","group of pirates");
        CyclingPortal.createTeam("StrawBalls","group of pirates with caps");
        int[] teams = CyclingPortal.getTeams();
        for (int team : teams) {
            System.out.println(team);
        }
        
        System.out.println(" ");
    
        CyclingPortal.removeTeam(0);
        int[] teams2 = CyclingPortal.getTeams();
        for (int team : teams2) {
            System.out.println(team);
            
        }

        System.out.println(" ");

        CyclingPortal.createRider(1,"Simon",1985);
        CyclingPortal.createRider(1,"Brian",1977);
        CyclingPortal.createRider(1,"Connor",1991);
        CyclingPortal.createRider(1,"Ernest",1991);
      

        // CyclingPortal.removeRider(2);
        int[] riders = CyclingPortal.getTeamRiders(1);
        for (int rider: riders) {
            System.out.println(rider);
        }
        
        System.out.println(" ");
        

        CyclingPortal.addCategorizedClimbToStage(0, 0.0, CheckpointType.C1, 0.2, 100.0);    
        CyclingPortal.addCategorizedClimbToStage(0, 2.0, CheckpointType.C1, 0.2, 100.0);
        CyclingPortal.addCategorizedClimbToStage(0, 3.0, CheckpointType.C1, 0.2, 100.0);
        CyclingPortal.addCategorizedClimbToStage(0, 4.0, CheckpointType.C1, 0.2, 100.0);
        CyclingPortal.addCategorizedClimbToStage(0, 5.0, CheckpointType.C1, 0.2, 100.0);
        // CyclingPortal.addIntermediateSprintToStage(0, 2.5);



        // CyclingPortal.removeCheckpoint(0);
        int[] existingCheckpoints = CyclingPortal.getStageCheckpoints(0);
        for (int checkpoint: existingCheckpoints){
            System.out.println(checkpoint);
        }

        
  
        LocalTime start = LocalTime.of(8, 0); 
        LocalTime checkpoint1 = LocalTime.of(8, 30);
        LocalTime checkpoint2 = LocalTime.of(9, 0); 
        LocalTime checkpoint3 = LocalTime.of(9, 15); 
        LocalTime finish = LocalTime.of(9, 30,0, 1); 

        LocalTime[] checkpointTimes = {start, checkpoint1, checkpoint2,checkpoint3, finish};
        CyclingPortal.registerRiderResultsInStage(0,0, checkpointTimes);
        LocalTime[] result = CyclingPortal.getRiderResultsInStage(0,0);
      


        
       
        for (LocalTime time : result) {
            System.out.println(time);
        }


        LocalTime start1 = LocalTime.of(8, 0); 
        LocalTime checkpoint11 = LocalTime.of(8, 30);
        LocalTime checkpoint21 = LocalTime.of(9, 0); 
        LocalTime checkpoint31 = LocalTime.of(9, 15); 
        LocalTime finish1 = LocalTime.of(9, 30,2,3); 

        LocalTime[] checkpointTimes1 = {start1, checkpoint11, checkpoint21,checkpoint31, finish1};
        CyclingPortal.registerRiderResultsInStage(0,1, checkpointTimes1);
        LocalTime[] result1 = CyclingPortal.getRiderResultsInStage(0,1);

        LocalTime start2 = LocalTime.of(8, 0); 
        LocalTime checkpoint12 = LocalTime.of(8, 30);
        LocalTime checkpoint22 = LocalTime.of(9, 0); 
        LocalTime checkpoint32 = LocalTime.of(9, 15); 
        LocalTime finish2 = LocalTime.of(9, 30,2, 5); 

        LocalTime[] checkpointTimes2 = {start2, checkpoint12, checkpoint22,checkpoint32, finish2};
        CyclingPortal.registerRiderResultsInStage(0,2, checkpointTimes2);
        LocalTime[] result2 = CyclingPortal.getRiderResultsInStage(0,2);

        LocalTime start3 = LocalTime.of(8, 0); 
        LocalTime checkpoint13 = LocalTime.of(8, 30);
        LocalTime checkpoint23 = LocalTime.of(9, 0); 
        LocalTime checkpoint33 = LocalTime.of(9, 15); 
        LocalTime finish3 = LocalTime.of(9, 30,2, 7); 

        LocalTime[] checkpointTimes3 = {start3, checkpoint13,checkpoint23,checkpoint33, finish3};
        CyclingPortal.registerRiderResultsInStage(0,3, checkpointTimes3);
        LocalTime[] result3 = CyclingPortal.getRiderResultsInStage(0,3);

        LocalTime adjusted = CyclingPortal.getRiderAdjustedElapsedTimeInStage(0, 3);
        System.out.println(adjusted);




    }}


