package it.polito.tdp.seriea.model;

public class TeamAndPoints implements Comparable<TeamAndPoints> {
	
	private Team team;
	private int points;
	public TeamAndPoints(Team team) {
		super();
		this.team = team;
		this.points = 0;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getPoints() {
		return points;
	}
	public void addPoints(int points) {
		this.points += points;
	}
	@Override
	public int compareTo(TeamAndPoints altro) {
		
		return -(this.points - altro.getPoints());
	}
	@Override
	public String toString() {
		return team + " " + points + "\n";
	}
	
	

}
