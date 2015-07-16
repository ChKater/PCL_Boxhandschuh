package de.luh.hci.pcl.boxhandschuh.model;

import java.util.ArrayList;
import java.util.List;

public class UserPojo {

	private double maxForce, maxSpeed;
	private int combinationCount, punchCount, points;
	private List<Award> awards = new ArrayList<>();
	private List<Punch> trainingData = new ArrayList<>();
	private String username;

	public UserPojo() {
		super();
		trainingData = new ArrayList<>();
		awards = new ArrayList<>();
	}
	
	

	public UserPojo(String username) {
		this();
		this.username = username;
	}



	public UserPojo(double maxForce, double maxSpeed, int combinationCount,
			int punchCount, int points, List<Award> awards,
			List<Punch> trainingData, String username) {
		super();
		this.maxForce = maxForce;
		this.maxSpeed = maxSpeed;
		this.combinationCount = combinationCount;
		this.punchCount = punchCount;
		this.points = points;
		this.awards = awards;
		this.trainingData = trainingData;
		this.username = username;
	}

	public double getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(double maxForce) {
		this.maxForce = maxForce;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public int getCombinationCount() {
		return combinationCount;
	}

	public void setCombinationCount(int combinationCount) {
		this.combinationCount = combinationCount;
	}

	public int getPunchCount() {
		return punchCount;
	}

	public void setPunchCount(int punchCount) {
		this.punchCount = punchCount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public List<Punch> getTrainingData() {
		return trainingData;
	}

	public void setTrainingData(List<Punch> trainingData) {
		this.trainingData = trainingData;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
