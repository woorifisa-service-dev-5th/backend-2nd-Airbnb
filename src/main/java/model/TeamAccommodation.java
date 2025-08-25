package model;

public class TeamAccommodation {
	int teamID;
	int accomID;
	int like;

	public TeamAccommodation(int teamID, int accomID, int like) {
		super();
		this.teamID = teamID;
		this.accomID = accomID;
		this.like = like;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	public int getAccomID() {
		return accomID;
	}

	public void setAccomID(int accomID) {
		this.accomID = accomID;
	}

}
