package cycling;

import java.io.Serializable;

public class Checkpoint implements Serializable {
    protected CheckpointType type;
    protected Double location;
    protected Double length;
    protected int id;
    protected static int CheckpointIDCounter = 0;

    public Checkpoint(){
        this.id = CheckpointIDCounter++;
    }

    public Checkpoint(Double location){
        this.location = location;
        this.id = CheckpointIDCounter++;
    }
    public Checkpoint(CheckpointType type, Double location, Double length){
        this.type=type;
        this.location = location;
        this.length = length;
        this.id = CheckpointIDCounter++;
    }

    public int getID(){
        return this.id;
    }

    public CheckpointType getCheckPointType(){
        return type;
    }

    public static void resetCounter(){
        CheckpointIDCounter = 0;
    }
}
