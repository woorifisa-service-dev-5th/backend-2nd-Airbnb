package model;

public class TeamAccommodation {
	int teamID;
	int accomID;

	public TeamAccommodation(int teamID, int accomID) {
		super();
		this.teamID = teamID;
		this.accomID = accomID;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getAccomID() {
		return accomID;
	}

	public void setAccomID(int accomID) {
		this.accomID = accomID;
	}

}
