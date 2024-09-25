package cycling;

public class MountainCheckpoint extends Checkpoint{

    private double averageGradient;
    private int id;

    public MountainCheckpoint(){
        this.id = super.CheckpointIDCounter++;
    }
    public MountainCheckpoint(CheckpointType type, Double location, Double length, Double averageGradient){
        /*this.type=type;
        this.location = location;
        this.length = length;*/
        super(type, location, length);
        this.id = super.CheckpointIDCounter++;
        this.averageGradient = averageGradient;
    }


}
