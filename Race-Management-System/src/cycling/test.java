package cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class test {
    public static void main(String[] args) throws IllegalNameException, InvalidNameException, IDNotRecognisedException, InvalidLengthException, DuplicatedResultException, InvalidCheckpointTimesException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException, IOException, ClassNotFoundException {
        CyclingPortalImpl impl = new CyclingPortalImpl();

        /*LocalTime[] times1 = {
            LocalTime.of(1,0,0),
            LocalTime.of(12,30,29),
            LocalTime.of(8,42,45),
            LocalTime.of(23,30,1),
            LocalTime.of(15,0,0),
            LocalTime.of(15,0,1),
                LocalTime.of(1,0,0)
        };

        LocalTime[] times2 = {
                LocalTime.of(1,0,0),
                LocalTime.of(12,30,29),
                LocalTime.of(8,42,45),
                LocalTime.of(23,30,1),
                LocalTime.of(15,0,0),
                LocalTime.of(15,0,1),
                LocalTime.of(12,30,29)
        };
        LocalTime[] times3 = {
                LocalTime.of(1,0,0),
                LocalTime.of(12,30,29),
                LocalTime.of(8,42,45),
                LocalTime.of(23,30,1),
                LocalTime.of(15,0,0),
                LocalTime.of(15,0,1),
                LocalTime.of(8,42,45)
        };
        LocalTime[] times4 = {
                LocalTime.of(1,0,0),
                LocalTime.of(12,30,29),
                LocalTime.of(8,42,45),
                LocalTime.of(23,30,1),
                LocalTime.of(15,0,0),
                LocalTime.of(15,0,1),
                LocalTime.of(23,30,1)
        };
        LocalTime[] times5 = {
                LocalTime.of(1,0,0),
                LocalTime.of(12,30,29),
                LocalTime.of(8,42,45),
                LocalTime.of(23,30,1),
                LocalTime.of(15,0,0),
                LocalTime.of(15,0,1),
                LocalTime.of(15,0,0)
        };
        LocalTime[] times6 = {
                LocalTime.of(1,0,0),
                LocalTime.of(12,30,29),
                LocalTime.of(8,42,45),
                LocalTime.of(23,30,1),
                LocalTime.of(15,0,0),
                LocalTime.of(15,0,1),
                LocalTime.of(15,0,1)
        };
        LocalTime[] times7 = {
                LocalTime.of(1,0,0),
                LocalTime.of(12,30,29),
                LocalTime.of(8,42,45),
                LocalTime.of(23,30,1),
                LocalTime.of(15,0,0),
                LocalTime.of(15,0,1),
                LocalTime.of(23,59,59)
        };


        //Stage stage = new Stage("Roger", "Its a stage, in fact its a pretty good one", 100.0, LocalDateTime.of(2024, 1, 1,0,0,0), StageType.MEDIUM_MOUNTAIN);
        impl.createRace("race1", "very cool race");
        impl.addStageToRace(0, "stage1", "cool", 100.0, LocalDateTime.of(2024, 1, 1,0,0,0), StageType.MEDIUM_MOUNTAIN);
        impl.createTeam("redbull", "taste nice");
        impl.createRider(0, "Ron", 2000);
        impl.createRider(0, "Dan", 2001);
        impl.createRider(0, "Tim", 2000);
        impl.createRider(0, "Sam", 2000);
        impl.createRider(0, "Bob", 1989);
        impl.createRider(0, "Ink", 1996);
        impl.createRider(0, "Til", 2004);
        impl.addCategorizedClimbToStage(0, 0.0, CheckpointType.C1,0.2, 100.0);
        impl.addCategorizedClimbToStage(0, 100.0, CheckpointType.C2,0.25, 100.0);
        impl.addCategorizedClimbToStage(0, 200.0, CheckpointType.C1,0.2, 100.0);
        impl.addCategorizedClimbToStage(0, 300.0, CheckpointType.C3,0.26, 100.0);
        impl.addCategorizedClimbToStage(0, 400.0, CheckpointType.C4,0.27, 100.0);
        impl.addCategorizedClimbToStage(0, 500.0, CheckpointType.HC,0.3, 100.0);
        impl.addCategorizedClimbToStage(0, 600.0, CheckpointType.C1,0.2, 100.0);
        impl.registerRiderResultsInStage(0, 0, times2);
        impl.registerRiderResultsInStage(0, 1, times1);
        impl.registerRiderResultsInStage(0, 2, times5);
        impl.registerRiderResultsInStage(0, 3, times4);
        impl.registerRiderResultsInStage(0, 4, times3);
        impl.registerRiderResultsInStage(0, 5, times6);
        impl.registerRiderResultsInStage(0, 6, times7);
        int[] rankedRiders = impl.getRidersRankInStage(0);
        impl.saveCyclingPortal("CyclingPortalSave.dat");*/
        impl.loadCyclingPortal("CyclingPortalSave.dat");
        int[] rankedRiders = impl.getRidersRankInStage(0);
        for(int r: rankedRiders){
            System.out.println(r);
        }

    }
}
