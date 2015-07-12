package de.luh.hci.pcl.boxhandschuh.model;

public class User {
    
    private double maxForce;
    
    private double maxSpeed;
    
    private int combinationCount;
    
    private int punchCount;
    
    private String username;
    
    public User(String username) {
        super();
        this.username = username;
        // TODO Auto-generated constructor stub
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

    public String getUsername() {
        return username;
    }
    

}
