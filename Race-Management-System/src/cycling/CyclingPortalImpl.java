package cycling;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.time.LocalTime;
import java.time.LocalDateTime;



// create array lists to store all riders, teams, stages, checkpoints etc

public class CyclingPortalImpl implements CyclingPortal {

	private HashMap<Integer, Integer> checkpointStageDictionary = new HashMap<>();
	private HashMap<Integer, Integer> stageRaceDictionary = new HashMap<>();
	private ArrayList<Rider> myRiders = new ArrayList<>();

	private ArrayList<Team> myTeams = new ArrayList<>();
	private ArrayList<Race> myRaces = new ArrayList<>();

	private ArrayList<Stage> myStages = new ArrayList<>();
	private ArrayList<Checkpoint> myCheckpoints = new ArrayList<>();

	// public Map<Integer, int[]> teamRiders = new HashMap<>();



	@Override
	public int[] getRaceIds() {
		ArrayList<Integer> ids = new ArrayList<>();


		for(Race r: myRaces){
			if(r != null){
				ids.add(r.getId());
			}
		}

		int[]raceIds = new int[ids.size()];
		int j = 0;
		for(Integer id: ids){
			raceIds[j] = (int) id;
			j++;
		}
		return raceIds;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		if (name == null || name.isEmpty()) {
			throw new IllegalNameException("Race name cannot be null or empty");
		}
		if (name.length() > 100) {
			throw new InvalidNameException("Race name cannot be longer than 100 characters");
		}

		for (Race existingRace : myRaces) {
			if (existingRace.getName().equalsIgnoreCase(name)) {
				throw new IllegalNameException("Race name already exists");
			}
		}

		Race race = new Race(name, description);
		myRaces.add(race);

		return race.getId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {

		if (raceId < 0 || raceId >= myRaces.size()) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " not found");
		}

		//make sure to check that the race isn't null
		Race race = myRaces.get(raceId);

		if (race == null) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " not found");
		}

		String numberOfStages = Integer.toString(race.getNumberOfStages());
		String description = race.getDescription();
		String name = race.getName();
		String raceLength = Double.toString(race.getLength());

		return "The race: " + name+ "\n With ID,  "+ Integer.toString(raceId) +
				"\n Is described as: \n" + description + "\n and has "+ numberOfStages+
				" stages and a length of, "+ raceLength + "km.";
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {

		if (raceId < 0 || raceId >= myRaces.size()) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " not found");
		}


		myRaces.set(raceId, null);
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		if (raceId < 0 || raceId >= myRaces.size() || myRaces.get(raceId) == null) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " not found");
		}
		//make sure to check that the race isn't null
		return myRaces.get(raceId).getNumberOfStages();
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime,
							  StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {

		if (raceId < 0 || raceId >= myRaces.size()) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " not found");
		}

		Race race = myRaces.get(raceId);
		if (race == null) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " is removed");
		}

		if (stageName == null || stageName.isEmpty()) {
			throw new IllegalNameException("Stage name cannot be null or empty");
		}
		if (stageName.length() > 100) {
			throw new InvalidNameException("Stage name cannot be longer than 100 characters");
		}

		if (length < 5.0 ) {
			throw new InvalidLengthException("Stage length cannot be less than 5.0 km");
		}

		for (Stage existingStage : myStages) {
			if (existingStage.getName().equalsIgnoreCase(stageName)) {
				throw new IllegalNameException("Race name already exists");
			}
		}


		Stage stage = new Stage(stageName, description, length, startTime, type);
		myStages.add(stage);
		//make sure to check that the race isn't null
		myRaces.get(raceId).addStageToRace(stage);
		stageRaceDictionary.put(stage.getID(), raceId);

		return stage.getID();
	}


	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {

		Race race = myRaces.get(raceId);
		if (race == null) {
			throw new IDNotRecognisedException("Race with ID " + raceId + "has been removed");
		}

		if (raceId < 0 || raceId >= myRaces.size()) {
			throw new IDNotRecognisedException("Race with ID " + raceId + " not found");
		}
		ArrayList<Stage> stages = race.getRaceStages();

		int[] raceStages =  new int[stages.size()];

		for(int i = 0; i<= stages.size()-1; i++){
			raceStages[i] = stages.get(i).getID();
		}

		return raceStages;
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {

		if (stageId < 0 || stageId >= myStages.size()) {
			throw new IDNotRecognisedException("Race with ID " + stageId + " not found");
		}

		Stage stage = myStages.get(stageId);



		if (stage == null) {
			throw new IDNotRecognisedException("Race with ID " + stageId + "has been removed");
		}

		//make sure to check that the stage value is not null
		return myStages.get(stageId).getStageLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {

		if (stageId < 0 || stageId >= myStages.size()) {
			throw new IDNotRecognisedException("Race with ID " + stageId + " not found");
		}

		Stage stage = myStages.get(stageId);



		if (stage == null) {
			throw new IDNotRecognisedException("Race with ID " + stageId + "has been removed");
		}

		//make sure to check that the stage value is not null
		myRaces.get(stageRaceDictionary.get(stageId)).removeStageById(stageId);
		myStages.set(stageId, null);


	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, CheckpointType type, Double averageGradient,
										  Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {

		if (stageId < 0 || stageId >= myRaces.size()) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
		}

		Stage stage = myStages.get(stageId);
		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " is removed");
		}

		if (stage.getState() == StageState.READY) {
			throw new InvalidStageStateException("Stage is in 'waiting for results' state and cannot be modified.");
		}

		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoint.");
		}

		double stageLength = myStages.get(stageId).getStageLength();


		if (location < 0 ||  location > stageLength) {
			throw new InvalidLocationException("Location cannot be outside the bounds of the stage");
		}



		MountainCheckpoint mtnCheckpoint = new MountainCheckpoint(type, location, length, averageGradient);
		myCheckpoints.add(mtnCheckpoint);
		myStages.get(stageId).addCategorisedClimbToStage(mtnCheckpoint);
		checkpointStageDictionary.put(mtnCheckpoint.getID(), stageId);
		return mtnCheckpoint.getID();
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {


		if (stageId < 0 || stageId >= myRaces.size()) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
		}

		Stage stage = myStages.get(stageId);
		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " is removed");
		}

		if (stage.getState() == StageState.READY) {
			throw new InvalidStageStateException("Stage is in 'waiting for results' state and cannot be modified.");
		}

		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot contain any checkpoint.");
		}

		double stageLength = myStages.get(stageId).getStageLength();


		if (location < 0 ||  location > stageLength) {
			throw new InvalidLocationException("Location cannot be outside the bounds of the stage");
		}


		Checkpoint sprint = new Checkpoint(location);
		myCheckpoints.add(sprint);
		myStages.get(stageId).addIntermediateSprintToStage(sprint);
		checkpointStageDictionary.put(sprint.getID(), stageId);
		return sprint.getID();
	}

	@Override
	public void removeCheckpoint(int checkpointId) throws IDNotRecognisedException, InvalidStageStateException {
		if (checkpointId < 0 || checkpointId >= myCheckpoints.size()) {
			throw new IDNotRecognisedException("Checkpoint with ID " + checkpointId + " not found");
		}




		myCheckpoints.set(checkpointId, null);
		int stage = checkpointStageDictionary.get(checkpointId);
		Stage s = myStages.get(stage);
		s.removeCheckpoint(checkpointId);
		checkpointStageDictionary.remove(checkpointId);

		if (s.getState() == StageState.READY) {
			throw new InvalidStageStateException("Stage is in 'waiting for results' state and cannot be modified.");
		}
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		if (stageId < 0 || stageId >= myStages.size()) {
			throw new IDNotRecognisedException("Race with ID " + stageId + " not found");
		}


		Stage stage = myStages.get(stageId);



		if (stage == null) {
			throw new IDNotRecognisedException("Race with ID " + stageId + "has been removed");
		}

		if (stage.getState() == StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage is already in 'waiting for results' state.");
		}

		// Updates the state of the stage to "waiting for results"
		stage.setState();

	}

	@Override
	public int[] getStageCheckpoints(int stageId) throws IDNotRecognisedException {

		Stage stage = myStages.get(stageId);

		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + "has been removed");
		}

		if (stageId < 0 || stageId >= myRaces.size()) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
		}
		ArrayList<Checkpoint> checkpoints = stage.getStageCheckpoints();

		int[] stageCheckpoints =  new int[checkpoints.size()];

		for(int i = 0; i<= checkpoints.size()-1; i++){
			if(checkpoints.get(i) != null){
				stageCheckpoints[i] = checkpoints.get(i).getID();
			}
		}

		return stageCheckpoints;

	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {

		if (name == null || name.isEmpty()) {
			throw new InvalidNameException("Team name cannot be null or empty");
		}
		if (name.length() > 100) {
			throw new InvalidNameException("Team name cannot be longer than 100 characters");
		}


		for (Team existingTeam : myTeams) {
			if (existingTeam.getName().equalsIgnoreCase(name)) {
				throw new IllegalNameException("Team name already exists");
			}
		}


		Team team = new Team(name, description);
		this.myTeams.add(team);

		return team.getTeamId();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {

		if (teamId < 0 || teamId >= myRaces.size()-1) {
			throw new IDNotRecognisedException("Team with ID " + teamId + " not found");
		}

		//make sure to check that the race isn't null
		Team team = myTeams.get(teamId);

		if (team == null) {
			throw new IDNotRecognisedException("Team with ID " + teamId + " not found");
		}
		Team teamToRemove = null;
		Iterator<Team> iterator = myTeams.iterator();
		while (iterator.hasNext()) {
			Team t = iterator.next();
			if (t.getTeamId() == teamId) {
				teamToRemove = t;
				iterator.remove(); // Removes the current element safely
			}
		}




	}


	@Override
	public int[] getTeams() {



		int[] result = new int[myTeams.size()];
		for (int i = 0; i < myTeams.size(); i++) {
			result[i] = myTeams.get(i).getTeamId();
		}
		return result;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {



		int[] exception = new int[myTeams.size()];
		for (int i = 0; i < myTeams.size(); i++) {
			exception[i] = myTeams.get(i).getTeamId();
		}

		boolean found = false;
		for (int id : exception) {
			if (id == teamId) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IDNotRecognisedException("Team with ID " + teamId + " not found");
		}

		List<Integer> teamRiders = new ArrayList<>();
		for (Rider rider : myRiders) {
			if (rider.getTeamId() == teamId) {
				teamRiders.add(rider.getRiderId());
			}
		}


		int[] result = new int[teamRiders.size()];
		for (int i = 0; i < teamRiders.size(); i++) {
			result[i] = teamRiders.get(i);
		}

		return result;
		// should throw an exception do later

	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {

		int[] result = new int[myTeams.size()];
		for (int i = 0; i < myTeams.size(); i++) {
			result[i] = myTeams.get(i).getTeamId();
		}

		boolean found = false;
		for (int id : result) {
			if (id == teamID) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IDNotRecognisedException("Team with ID " + teamID + " not found");
		}

		// Check if name is empty or null
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty or null");
		}


		String[] inappropriateWords = {"bitch", "retard", "asshole", "fuck", "whore", "pussy", "wanker", "boob", "tit", };
		String lowerCaseName = name.toLowerCase();
		for (String word : inappropriateWords) {
			if (lowerCaseName.contains(word)) {
				throw new IllegalArgumentException("Inappropriate name: " + name);
			}
		}

		Rider rider = new Rider(teamID, name, yearOfBirth);
		this.myRiders.add(rider);


		return rider.getRiderId();

	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		Rider riderToRemove = null; //just go over the indexes
		for (Rider rider : myRiders) {
			if (rider.getRiderId() == riderId) {
				riderToRemove = rider;
				break;
			}
		}
		if (riderToRemove != null) {
			myRiders.remove(riderToRemove);
		} else {
			throw new IDNotRecognisedException("Rider with ID " + riderId + " not found."); // dont need the message? message should be in the exception class
		}


	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointTimesException,
			InvalidStageStateException {


		Stage stage = myStages.get(stageId);
		if(stage.getResults().containsKey(riderId)){
			throw new DuplicatedResultException("Rider with ID" + riderId + " has had his results added already");
		}

		for(int i = 0; i<checkpoints.length-1; i++){
			if(checkpoints[i+1].isBefore(checkpoints[i])){
				throw new InvalidCheckpointTimesException("the checkpoints are not in chronological order");
			}
		}
		// if (stage == null) {
		// 	throw new IDNotRecognisedException("Stage with ID " + stageId + "has been removed");
		// }

		if (stageId < 0 || stageId >= myRaces.size()) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
		}

		if (stage.getState() != StageState.WAITING_FOR_RESULTS) {
			throw new InvalidStageStateException("Stage is not in 'waiting for results' state.");
		}

		int[] exception = new int[myRiders.size()];
		for (int i = 0; i < myRiders.size(); i++) {
			exception[i] = myRiders.get(i).getRiderId();
		}

		boolean found = false;
		for (int id : exception) {
			if (id == riderId) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IDNotRecognisedException("Rider with ID " + riderId + " not found");

		}


		stage.registerRiderResultsInStage(riderId, checkpoints);



	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {

		Stage stage = myStages.get(stageId);
		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + "has been removed");
		}

		if (stageId < 0 || stageId >= myRaces.size()) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
		}

		int[] exception = new int[myRiders.size()];
		for (int i = 0; i < myRiders.size(); i++) {
			exception[i] = myRiders.get(i).getRiderId();
		}

		boolean found = false;
		for (int id : exception) {
			if (id == riderId) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IDNotRecognisedException("Rider with ID " + riderId + " not found");
		}
		LocalTime[] result = stage.getRiderResultsInStage(riderId);

		return result;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {


		Stage stage = myStages.get(stageId);
		if (stage == null) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + "has been removed");
		}

		if (stageId < 0 || stageId >= myRaces.size()) {
			throw new IDNotRecognisedException("Stage with ID " + stageId + " not found");
		}

		int[] exception = new int[myRiders.size()];
		for (int i = 0; i < myRiders.size(); i++) {
			exception[i] = myRiders.get(i).getRiderId();
		}

		boolean found = false;
		for (int id : exception) {
			if (id == riderId) {
				found = true;
				break;
			}
		}

		if (!found) {
			throw new IDNotRecognisedException("Rider with ID " + riderId + " not found");
		}


		LocalTime result = stage.getRiderAdjustedElapsedTimeInStages(riderId);


		return result;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		try {
			Stage stage = myStages.get(stageId);
			if (stage == null) {
				throw new IDNotRecognisedException("Stage ID not recognized: " + stageId);
			}
			stage.deleteRiderResultsInStage(riderId);
		} catch (IDNotRecognisedException e) {
			throw new IDNotRecognisedException("Error deleting rider results in stage: " + e.getMessage());
		}

	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		try{
			Stage stage = myStages.get(stageId);
			if(stage == null){
				throw new IDNotRecognisedException("Stage id not recognised" + stageId);
			}
			return stage.getRidersRankInStage();

		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error getting riders rank: " + e.getMessage());
		}
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		try{
			Stage stage = myStages.get(stageId);
			if(stage == null){
				throw new IDNotRecognisedException("Id not recognised " + stageId);
			}
			return stage.getRankedAdjustedElapsedTimesInStages();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error: " + e.getMessage());
		}
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {

		try{
			Stage stage = myStages.get(stageId);
			if(stage == null){
				throw new IDNotRecognisedException("Id not recognised " + stageId);
			}
			return stage.getRiderPointsInStage();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error: " + e.getMessage());
		}
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		try{
			Stage stage = myStages.get(stageId);
			if(stage == null){
				throw new IDNotRecognisedException("Id not recognised " + stageId);
			}
			return stage.getRiderMountainPointsInStage();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error: " + e.getMessage());
		}

	}

	@Override
	public void eraseCyclingPortal() {
		Rider.resetCounter();
		Race.resetCounter();
		Stage.resetCounter();
		Team.resetCounter();
		Checkpoint.resetCounter();

		myCheckpoints.clear();
		myTeams.clear();
		myRaces.clear();
		myRiders.clear();
		myStages.clear();

		stageRaceDictionary.clear();
		checkpointStageDictionary.clear();
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
			// Save all the parameters to the file
			oos.writeObject(stageRaceDictionary);
			oos.writeObject(myRiders);
			oos.writeObject(myTeams);
			oos.writeObject(myRaces);
			oos.writeObject(myStages);
			oos.writeObject(myCheckpoints);

			System.out.println("Cycling portal saved successfully.");
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			throw e;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			// Load the serialized objects from the file
			stageRaceDictionary = (HashMap<Integer, Integer>) ois.readObject();
			myRiders = (ArrayList<Rider>) ois.readObject();
			myTeams = (ArrayList<Team>) ois.readObject();
			myRaces = (ArrayList<Race>) ois.readObject();
			myStages = (ArrayList<Stage>) ois.readObject();
			myCheckpoints = (ArrayList<Checkpoint>) ois.readObject();

			System.out.println("Cycling portal loaded successfully.");
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error loading cycling portal: " + e.getMessage());
			throw e;
		}

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {

		try {
			boolean raceFound = false;
			for (Race race : myRaces) {
				if (race != null && race.getName().equals(name)) {
					myRaces.set(race.getId(), null);
					raceFound = true;
					break; // No need to continue searching if the race is found and removed
				}
			}
			if (!raceFound) {
				throw new NameNotRecognisedException("Race name not recognized: " + name);
			}
		} catch (NameNotRecognisedException e) {
			System.err.println("Error: " + e.getMessage());
			// Re-throw the exception if necessary
			throw e;
		}
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {

		try{
			Race race = myRaces.get(raceId);
			if(race == null){
				throw new IDNotRecognisedException("Id not recognised " + raceId);
			}
			return race.getGeneralClassificationTimesInRace();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error: " + e.getMessage());
		}
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		try{
			Race race = myRaces.get(raceId);
			if(race == null){
				throw new IDNotRecognisedException("Id not recognised " + raceId);
			}
			return race.getRidersPointsInRace();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error: " + e.getMessage());
		}
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		try{
			Race race = myRaces.get(raceId);
			if(race == null){
				throw new IDNotRecognisedException("Id not recognised " + raceId);
			}
			return race.getRidersMountainPointsInRace();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error : " + e.getMessage());
		}
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {


		try{
			Race race = myRaces.get(raceId);
			if(race == null){
				throw new IDNotRecognisedException("Id not recognised " + raceId);
			}
			return race.getRidersGeneralClassificationRank();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error : " + e.getMessage());
		}
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {


		try{
			Race race = myRaces.get(raceId);
			if(race == null){
				throw new IDNotRecognisedException("Id not recognised " + raceId);
			}
			return race.getRidersPointClassificationRank();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error : " + e.getMessage());
		}
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {

		try{
			Race race = myRaces.get(raceId);
			if(race == null){
				throw new IDNotRecognisedException("Id not recognised " + raceId);
			}
			return race.getRidersMountainPointClassificationRank();
		}
		catch (IDNotRecognisedException e){
			throw new IDNotRecognisedException("Error : " + e.getMessage());
		}
	}

}
