package de.luh.hci.pcl.boxhandschuh.model;

import javafx.scene.image.Image;

public class Award {
	
	private String name;
	private String imagePath;
	private double minForce, minSpeed;
	private int minCombinationCount, minPunchCount, minPoints;

	public Award(String name, String image, double minForce, double minSpeed,
			int minCombinationCount, int minPunchCount, int minPoints) {
		super();
		this.name = name;
		this.imagePath = image;
		this.minForce = minForce;
		this.minSpeed = minSpeed;
		this.minCombinationCount = minCombinationCount;
		this.minPunchCount = minPunchCount;
		this.minPoints = minPoints;
	}
	
	public Award(String name, double minForce, double minSpeed,
			int minCombinationCount, int minPunchCount, int minPoints) {
		super();
		this.name = name;
		this.minForce = minForce;
		this.minSpeed = minSpeed;
		this.minCombinationCount = minCombinationCount;
		this.minPunchCount = minPunchCount;
		this.minPoints = minPoints;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return new Image(imagePath);
	}

	
	
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public double getMinForce() {
		return minForce;
	}

	public void setMinForce(double minForce) {
		this.minForce = minForce;
	}

	public double getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(double minSpeed) {
		this.minSpeed = minSpeed;
	}

	public int getMinCombinationCount() {
		return minCombinationCount;
	}

	public void setMinCombinationCount(int minCombinationCount) {
		this.minCombinationCount = minCombinationCount;
	}

	public int getMinPunchCount() {
		return minPunchCount;
	}

	public void setMinPunchCount(int minPunchCount) {
		this.minPunchCount = minPunchCount;
	}

	public int getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;
	}
	
	public boolean conditionsFullfilled(User user){
		return user.getMaxForce() >= minForce && user.getMaxSpeed() >= minSpeed && user.getCombinationCount() >= minCombinationCount && user.getPoints() >= minPoints && user.getPunchCount() >= minPunchCount;
	}

}
