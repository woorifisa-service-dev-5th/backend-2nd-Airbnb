package model;

import java.util.ArrayList;
import java.util.List;

public class Team {
	int teamID;
	String name;
	List<String> memberList;

	public Team(int teamID, String name, String[] memberList) {
		super();
		this.teamID = teamID;
		this.name = name;
		this.memberList = new ArrayList<>();
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<String> memberList) {
		this.memberList = memberList;
	}

}
