package de.luh.hci.pcl.boxhandschuh.model;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
	

    public final DoubleProperty maxForce = new SimpleDoubleProperty();
    public final DoubleProperty maxSpeed = new SimpleDoubleProperty();

    public final IntegerProperty combinationCount = new SimpleIntegerProperty();
    public final IntegerProperty punchCount = new SimpleIntegerProperty();
    public final IntegerProperty points = new SimpleIntegerProperty();

    private final ObservableList<Award> awards = FXCollections.observableArrayList();
    private final ObservableList<Punch> trainingData = FXCollections.observableArrayList();
   
    private String username;
    
    public User(){
    	super();
    }
    
    public User(UserPojo pojo){
    	this(pojo.getUsername());
    	setMaxForce(pojo.getMaxForce());
    	setMaxSpeed(pojo.getMaxSpeed());
    	setCombinationCount(pojo.getCombinationCount());
    	setPunchCount(pojo.getPunchCount());
    	setPoints(pojo.getPoints());
    	awards.addAll(pojo.getAwards());
    	trainingData.addAll(pojo.getTrainingData());
    }
    
    public User(String username) {
        this();
        this.username = username;
        // TODO Auto-generated constructor stub
    }

    public double getMaxForce() {
        return maxForce.get();
    }

    public void setMaxForce(double maxForce) {
        this.maxForce.set(maxForce);
    }

    public double getMaxSpeed() {
        return maxSpeed.get();
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed.set(maxSpeed);
    }

    public int getCombinationCount() {
        return combinationCount.get();
    }

    public void setCombinationCount(int combinationCount) {
        this.combinationCount.set(combinationCount);
    }

    public int getPunchCount() {
        return punchCount.get();
    }

    public void setPunchCount(int punchCount) {
        this.punchCount.set(punchCount);
    }

    public String getUsername() {
        return username;
    }

	public int getPoints() {
		return points.get();
	}

	public void setPoints(int points) {
		this.points.set(points);
	}
    
	public void addAward(Award award){
		if(!awards.contains(award)){
			awards.add(award);
		}
	}

	public ObservableList<Award> getAwards() {
		return awards;
	}

	public ObservableList<Punch> getTrainingData() {
		return trainingData;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public UserPojo getPojo(){
		return new UserPojo(getMaxForce(), getMaxSpeed(), getCombinationCount(), getPunchCount(), getPoints(), new ArrayList<>(getAwards()), new ArrayList<>(getTrainingData()), username);
	}
}
